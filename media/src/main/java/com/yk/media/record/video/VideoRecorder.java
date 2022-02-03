package com.yk.media.record.video;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.text.TextUtils;
import android.view.Surface;

import com.yk.media.opengl.egl.GLThread;
import com.yk.media.opengl.render.apply.RecordRender;
import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.RecordConfig;
import com.yk.media.record.config.VideoEncodeConfig;
import com.yk.media.record.listener.OnVideoRecordListener;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLContext;

public class VideoRecorder implements IVideoRecord {

    private GLThread glThread;
    private RecordThread recordThread;

    private RecordRender recordRender;

    private Context context;
    private EGLContext eglContext;
    private int fboTextureId;

    private AudioEncodeConfig audioEncodeConfig;
    private VideoEncodeConfig videoEncodeConfig;
    private RecordConfig recordConfig;

    private OnVideoRecordListener onVideoRecordListener;

    @Override
    public void prepare(Context context, EGLContext eglContext, int fboTextureId, boolean enableAudio,
                        int width, int height, String path, String bgmPath) {
        AudioEncodeConfig audioEncodeConfig = AudioEncodeConfig.getDefault();

        VideoEncodeConfig videoEncodeConfig = VideoEncodeConfig.getDefault()
                .setWidth(width)
                .setHeight(height);

        RecordConfig recordConfig = RecordConfig.getDefault()
                .setPath(path)
                .setEnableAudio(enableAudio)
                .setBgmPath(bgmPath);

        prepare(context, eglContext, fboTextureId, audioEncodeConfig, videoEncodeConfig, recordConfig);
    }

    @Override
    public void prepare(Context context, EGLContext eglContext, int fboTextureId,
                        AudioEncodeConfig audioEncodeConfig,
                        VideoEncodeConfig videoEncodeConfig,
                        RecordConfig recordConfig) {
        cancelRecord();

        this.context = context;
        this.eglContext = eglContext;
        this.fboTextureId = fboTextureId;

        this.audioEncodeConfig = audioEncodeConfig;
        this.videoEncodeConfig = videoEncodeConfig;
        this.recordConfig = recordConfig;
    }

    @Override
    public void startRecord() {
        recordRender = new RecordRender(context);
        recordRender.setTextureId(fboTextureId);

        recordThread = new RecordThread(
                audioEncodeConfig, videoEncodeConfig,
                recordConfig
        );
        recordThread.setOnVideoRecordListener(onVideoRecordListener);
        recordThread.init();

        glThread = new GLThread()
                .setInputSurface(recordThread.getInputSurface())
                .setShareEGLContext(eglContext)
                .setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
                .setRenderRate(videoEncodeConfig.getFrameRate())
                .setWidth(videoEncodeConfig.getWidth())
                .setHeight(videoEncodeConfig.getHeight())
                .setRenderer(recordRender);
        glThread.isCreate = true;
        glThread.isChange = true;

        recordThread.start();
        glThread.start();
    }

    @Override
    public void stopRecord() {
        stopRecordThread();
        stopGLThread();
    }

    @Override
    public void cancelRecord() {
        cancelRecordThread();
        stopGLThread();
    }

    private void stopRecordThread() {
        if (recordThread == null) {
            return;
        }
        recordThread.stopRecord();
        recordThread = null;
    }

    private void cancelRecordThread() {
        if (recordThread == null) {
            return;
        }
        recordThread.cancelRecord();
        recordThread = null;
    }

    private void stopGLThread() {
        if (glThread == null) {
            return;
        }
        glThread.onDestroy();
        glThread = null;
    }

    public void setOnVideoRecordListener(OnVideoRecordListener onVideoRecordListener) {
        this.onVideoRecordListener = onVideoRecordListener;
    }

    private static class RecordThread extends Thread {
        private MediaMuxer mediaMuxer;

        private MediaExtractor bgmExtractor;
        private int bgmTrackIndex = -1;

        private AudioRecord audioRecord;
        private MediaCodec audioCodec;

        private MediaCodec videoCodec;

        private final AudioEncodeConfig audioEncodeConfig;
        private final VideoEncodeConfig videoEncodeConfig;
        private final RecordConfig recordConfig;

        private int bufferSizeInBytes;

        private Surface inputSurface;

        private boolean isStopRecord = false;
        private boolean isCancelRecord = false;

        private OnVideoRecordListener onVideoRecordListener;

        public RecordThread(AudioEncodeConfig audioEncodeConfig, VideoEncodeConfig videoEncodeConfig, RecordConfig recordConfig) {
            this.audioEncodeConfig = audioEncodeConfig;
            this.videoEncodeConfig = videoEncodeConfig;
            this.recordConfig = recordConfig;
        }

        public void cancelRecord() {
            isCancelRecord = true;
            try {
                join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void stopRecord() {
            isStopRecord = true;
            try {
                join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void init() {
            initMuxer();
            initBgmExtractor();
            initAudio();
            initVideo();
        }

        private void initMuxer() {
            try {
                mediaMuxer = new MediaMuxer(recordConfig.getPath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            } catch (IOException e) {
                e.printStackTrace();
                mediaMuxer = null;
            }
        }

        private void initBgmExtractor() {
            String bgmPath = recordConfig.getBgmPath();
            if (TextUtils.isEmpty(bgmPath)) {
                return;
            }

            try {
                bgmExtractor = new MediaExtractor();
                bgmExtractor.setDataSource(bgmPath);

                int trackCount = bgmExtractor.getTrackCount();
                for (int i = 0; i < trackCount; i++) {
                    MediaFormat format = bgmExtractor.getTrackFormat(i);
                    String mime = format.getString(MediaFormat.KEY_MIME);
                    if (!TextUtils.isEmpty(mime) && mime.startsWith("audio/")) {
                        addBgmToMuxer(format);
                        bgmExtractor.selectTrack(i);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                bgmExtractor = null;
                bgmTrackIndex = -1;
            }
        }

        private void addBgmToMuxer(MediaFormat mediaFormat) {
            if (mediaMuxer == null) {
                return;
            }
            bgmTrackIndex = mediaMuxer.addTrack(mediaFormat);
        }

        private void initAudio() {
            if (!recordAudio()) {
                return;
            }

            bufferSizeInBytes = AudioRecord.getMinBufferSize(
                    audioEncodeConfig.getSampleRate(),
                    audioEncodeConfig.getChannelConfig(),
                    audioEncodeConfig.getAudioFormat()
            );

            audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    audioEncodeConfig.getSampleRate(),
                    audioEncodeConfig.getChannelConfig(),
                    audioEncodeConfig.getAudioFormat(),
                    bufferSizeInBytes
            );

            MediaFormat format = MediaFormat.createAudioFormat(
                    audioEncodeConfig.getMime(),
                    audioEncodeConfig.getSampleRate(),
                    audioEncodeConfig.getChannelConfig() == AudioFormat.CHANNEL_IN_STEREO ? 2 : 1
            );
            format.setInteger(MediaFormat.KEY_BIT_RATE, audioEncodeConfig.getBitRate());
            format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, audioEncodeConfig.getMaxInputSize());

            try {
                audioCodec = MediaCodec.createEncoderByType(audioEncodeConfig.getMime());
                audioCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            } catch (IOException e) {
                e.printStackTrace();
                audioRecord = null;
                audioCodec = null;
            }
        }

        private void initVideo() {
            MediaFormat format = MediaFormat.createVideoFormat(
                    videoEncodeConfig.getMime(),
                    videoEncodeConfig.getWidth(), videoEncodeConfig.getHeight()
            );
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, videoEncodeConfig.getFrameRate());
            format.setInteger(MediaFormat.KEY_BIT_RATE,
                    videoEncodeConfig.getWidth() * videoEncodeConfig.getHeight() * 4);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, videoEncodeConfig.getiFrameInterval());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                format.setInteger(MediaFormat.KEY_PROFILE, MediaCodecInfo.CodecProfileLevel.AVCProfileMain);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    format.setInteger(MediaFormat.KEY_LEVEL, MediaCodecInfo.CodecProfileLevel.AVCLevel3);
                }
            }

            try {
                videoCodec = MediaCodec.createEncoderByType(videoEncodeConfig.getMime());
                videoCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
                inputSurface = videoCodec.createInputSurface();
            } catch (IOException e) {
                e.printStackTrace();
                inputSurface = null;
                videoCodec = null;
            }
        }

        @Override
        public void run() {
            super.run();
            record();
        }

        private void record() {
            if (mediaMuxer == null) {
                onRecordError(new IllegalArgumentException("mediaMuxer is null"));
                return;
            }

            if (recordAudio() && audioCodec == null) {
                onRecordError(new IllegalArgumentException("audioCodec is null"));
                return;
            }

            if (videoCodec == null) {
                onRecordError(new IllegalArgumentException("videoCodec is null"));
                return;
            }

            boolean isStartMuxer = false; // 合成是否开始
            isCancelRecord = false;
            isStopRecord = false;

            long audioPts = 0;
            long videoPts = 0;

            int audioTrackIndex = -1;
            int videoTrackIndex = -1;

            if (recordAudio()) {
                audioRecord.startRecording();
                audioCodec.start();
            }
            videoCodec.start();

            onRecordStart(audioEncodeConfig, videoEncodeConfig, recordConfig);

            MediaCodec.BufferInfo audioInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo videoInfo = new MediaCodec.BufferInfo();

            for (; ; ) {
                if (isStopRecord || isCancelRecord) {
                    writeBgmToMuxer(videoInfo.presentationTimeUs);
                    release();
                    break;
                }

                if (recordAudio()) {
                    // 将AudioRecord录制的PCM原始数据送入编码器
                    int audioInputBufferId = audioCodec.dequeueInputBuffer(0);
                    if (audioInputBufferId >= 0) {
                        ByteBuffer inputBuffer = audioCodec.getInputBuffer(audioInputBufferId);
                        int readSize = -1;
                        if (inputBuffer != null) {
                            readSize = audioRecord.read(inputBuffer, bufferSizeInBytes);
                        }
                        if (readSize >= 0) {
                            audioCodec.queueInputBuffer(audioInputBufferId, 0, readSize, System.nanoTime() / 1000, 0);
                        }
                    }
                }

                // 获取从surface直接编码得到的数据，写入Muxer
                int videoOutputBufferId = videoCodec.dequeueOutputBuffer(videoInfo, 0);
                if (videoOutputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    videoTrackIndex = mediaMuxer.addTrack(videoCodec.getOutputFormat());
                    if (!recordAudio() || (audioTrackIndex != -1 && !isStartMuxer)) {
                        isStartMuxer = true;
                        mediaMuxer.start();
                    }
                } else if (videoOutputBufferId >= 0) {
                    ByteBuffer outputBuffer = videoCodec.getOutputBuffer(videoOutputBufferId);
                    if (outputBuffer != null && videoInfo.size != 0 && isStartMuxer) {
                        outputBuffer.position(videoInfo.offset);
                        outputBuffer.limit(videoInfo.offset + videoInfo.size);
                        if (videoPts == 0) {
                            videoPts = videoInfo.presentationTimeUs;
                        }
                        videoInfo.presentationTimeUs = videoInfo.presentationTimeUs - videoPts;
                        onRecording(videoInfo.presentationTimeUs);
                        mediaMuxer.writeSampleData(videoTrackIndex, outputBuffer, videoInfo);
                    }
                    videoCodec.releaseOutputBuffer(videoOutputBufferId, false);
                }

                if (recordAudio()) {
                    // 获取音频编码数据，写入Muxer
                    int audioOutputBufferId = audioCodec.dequeueOutputBuffer(audioInfo, 0);
                    if (audioOutputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                        audioTrackIndex = mediaMuxer.addTrack(audioCodec.getOutputFormat());
                        if (videoTrackIndex != -1 && !isStartMuxer) {
                            isStartMuxer = true;
                            mediaMuxer.start();
                        }
                    } else if (audioOutputBufferId >= 0) {
                        ByteBuffer outputBuffer = audioCodec.getOutputBuffer(audioOutputBufferId);
                        if (outputBuffer != null && audioInfo.size != 0 && isStartMuxer) {
                            outputBuffer.position(audioInfo.offset);
                            outputBuffer.limit(audioInfo.offset + audioInfo.size);
                            if (audioPts == 0) {
                                audioPts = audioInfo.presentationTimeUs;
                            }
                            audioInfo.presentationTimeUs = audioInfo.presentationTimeUs - audioPts;
                            mediaMuxer.writeSampleData(audioTrackIndex, outputBuffer, audioInfo);
                        }
                        audioCodec.releaseOutputBuffer(audioOutputBufferId, false);
                    }
                }
            }
        }

        private void writeBgmToMuxer(long pts) {
            if (isCancelRecord) {
                return;
            }

            if (bgmExtractor == null) {
                return;
            }

            if (bgmTrackIndex == -1) {
                return;
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(500 * 1024);
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            for (; ; ) {
                int readSize = bgmExtractor.readSampleData(buffer, 0);
                if (readSize <= 0) {
                    break;
                }
                info.offset = 0;
                info.size = readSize;
                info.presentationTimeUs = bgmExtractor.getSampleTime();
                mediaMuxer.writeSampleData(bgmTrackIndex, buffer, info);
                if (info.presentationTimeUs >= pts) {
                    break;
                }
                bgmExtractor.advance();
            }
        }

        private void release() {
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
            }

            if (audioCodec != null) {
                audioCodec.stop();
                audioCodec.release();
                audioCodec = null;
            }

            if (videoCodec != null) {
                videoCodec.stop();
                videoCodec.release();
                videoCodec = null;
            }

            if (mediaMuxer != null) {
                mediaMuxer.stop();
                mediaMuxer.release();
                mediaMuxer = null;
            }

            if (isCancelRecord) {
                File file = new File(recordConfig.getPath());
                if (file.exists()) {
                    file.delete();
                }
                onRecordCancel();
                return;
            }

            if (isStopRecord) {
                onRecordStop(recordConfig);
            }
        }

        private boolean recordAudio() {
            return TextUtils.isEmpty(recordConfig.getBgmPath()) && recordConfig.isEnableAudio();
        }

        public Surface getInputSurface() {
            return inputSurface;
        }

        private void onRecordStart(AudioEncodeConfig audioEncodeConfig, VideoEncodeConfig videoEncodeConfig, RecordConfig recordConfig) {
            if (onVideoRecordListener == null) {
                return;
            }
            onVideoRecordListener.onRecordStart(audioEncodeConfig, videoEncodeConfig, recordConfig);
        }

        private void onRecording(long pts) {
            if (onVideoRecordListener == null) {
                return;
            }
            onVideoRecordListener.onRecording(pts);
        }

        private void onRecordStop(RecordConfig recordConfig) {
            if (onVideoRecordListener == null) {
                return;
            }
            onVideoRecordListener.onRecordStop(recordConfig);
        }

        private void onRecordCancel() {
            if (onVideoRecordListener == null) {
                return;
            }
            onVideoRecordListener.onRecordCancel();
        }

        private void onRecordError(Exception e) {
            if (onVideoRecordListener == null) {
                return;
            }
            onVideoRecordListener.onRecordError(e);
        }

        public void setOnVideoRecordListener(OnVideoRecordListener onVideoRecordListener) {
            this.onVideoRecordListener = onVideoRecordListener;
        }
    }
}
