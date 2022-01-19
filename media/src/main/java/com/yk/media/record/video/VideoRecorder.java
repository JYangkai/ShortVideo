package com.yk.media.record.video;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.Surface;

import com.yk.media.camera.CameraSize;
import com.yk.media.opengl.egl.GLThread;
import com.yk.media.opengl.render.apply.RecordRender;
import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.CameraConfig;
import com.yk.media.record.config.MicConfig;
import com.yk.media.record.config.RecordConfig;
import com.yk.media.record.config.VideoEncodeConfig;

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

    private MicConfig micConfig;
    private CameraConfig cameraConfig;
    private AudioEncodeConfig audioEncodeConfig;
    private VideoEncodeConfig videoEncodeConfig;
    private RecordConfig recordConfig;

    @Override
    public void prepare(Context context, EGLContext eglContext, int fboTextureId, boolean enableAudio, int facing, CameraSize cameraSize, String path) {
        MicConfig micConfig = MicConfig.getDefault()
                .setEnable(enableAudio);

        CameraConfig cameraConfig = CameraConfig.getDefault()
                .setFacing(facing)
                .setCameraSize(cameraSize);

        AudioEncodeConfig audioEncodeConfig = AudioEncodeConfig.getDefault();

        VideoEncodeConfig videoEncodeConfig = VideoEncodeConfig.getDefault()
                .setWidth(cameraSize.getHeight())
                .setHeight(cameraSize.getWidth());

        RecordConfig recordConfig = RecordConfig.getDefault()
                .setPath(path);

        prepare(context, eglContext, fboTextureId, micConfig, cameraConfig, audioEncodeConfig, videoEncodeConfig, recordConfig);
    }

    @Override
    public void prepare(Context context, EGLContext eglContext, int fboTextureId, MicConfig micConfig, CameraConfig cameraConfig, AudioEncodeConfig audioEncodeConfig, VideoEncodeConfig videoEncodeConfig, RecordConfig recordConfig) {
        this.context = context;
        this.eglContext = eglContext;
        this.fboTextureId = fboTextureId;

        this.micConfig = micConfig;
        this.cameraConfig = cameraConfig;
        this.audioEncodeConfig = audioEncodeConfig;
        this.videoEncodeConfig = videoEncodeConfig;
        this.recordConfig = recordConfig;
    }

    @Override
    public void startRecord() {
        cancelRecord();

        recordRender = new RecordRender(context);
        recordRender.setTextureId(fboTextureId);

        recordThread = new RecordThread(
                micConfig, cameraConfig,
                audioEncodeConfig, videoEncodeConfig,
                recordConfig
        );
        recordThread.init();

        glThread = new GLThread()
                .setInputSurface(recordThread.getInputSurface())
                .setShareEGLContext(eglContext)
                .setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
                .setRenderRate(videoEncodeConfig.getFrameRate())
                .setWidth(videoEncodeConfig.getWidth())
                .setHeight(videoEncodeConfig.getHeight())
                .setRenderer(recordRender);
        glThread.isChange = true;
        glThread.isChange = true;

        recordThread.start();
        glThread.start();
    }

    @Override
    public void stopRecord() {
        cancelRecordThread();
        stopGLThread();
    }

    @Override
    public void cancelRecord() {
        stopRecordThread();
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

    private static class RecordThread extends Thread {
        private MediaMuxer mediaMuxer;

        private AudioRecord audioRecord;
        private MediaCodec audioCodec;

        private MediaCodec videoCodec;

        private final MicConfig micConfig;
        private final CameraConfig cameraConfig;
        private final AudioEncodeConfig audioEncodeConfig;
        private final VideoEncodeConfig videoEncodeConfig;
        private final RecordConfig recordConfig;

        private int bufferSizeInBytes;

        private Surface inputSurface;

        private boolean isStopRecord = false;
        private boolean isCancelRecord = false;

        public RecordThread(MicConfig micConfig, CameraConfig cameraConfig, AudioEncodeConfig audioEncodeConfig, VideoEncodeConfig videoEncodeConfig, RecordConfig recordConfig) {
            this.micConfig = micConfig;
            this.cameraConfig = cameraConfig;
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

        private void initAudio() {
            bufferSizeInBytes = AudioRecord.getMinBufferSize(
                    audioEncodeConfig.getSampleRate(),
                    audioEncodeConfig.getChannelConfig(),
                    audioEncodeConfig.getAudioFormat()
            );

            audioRecord = new AudioRecord(
                    micConfig.getSource(),
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
            if (mediaMuxer == null || audioCodec == null || videoCodec == null) {
                return;
            }

            boolean isStartMuxer = false; // 合成是否开始
            isCancelRecord = false;
            isStopRecord = false;

            long audioPts = 0;
            long videoPts = 0;

            int audioTrackIndex = -1;
            int videoTrackIndex = -1;

            audioRecord.startRecording();
            audioCodec.start();
            videoCodec.start();

            MediaCodec.BufferInfo audioInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo videoInfo = new MediaCodec.BufferInfo();

            for (; ; ) {
                if (isStopRecord || isCancelRecord) {
                    release();
                    break;
                }

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

                // 获取从surface直接编码得到的数据，写入Muxer
                int videoOutputBufferId = videoCodec.dequeueOutputBuffer(videoInfo, 0);
                if (videoOutputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    videoTrackIndex = mediaMuxer.addTrack(videoCodec.getOutputFormat());
                    if (audioTrackIndex != -1 && !isStartMuxer) {
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
                        mediaMuxer.writeSampleData(videoTrackIndex, outputBuffer, videoInfo);
                    }
                    videoCodec.releaseOutputBuffer(videoOutputBufferId, false);
                }

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
        }

        public Surface getInputSurface() {
            return inputSurface;
        }
    }
}