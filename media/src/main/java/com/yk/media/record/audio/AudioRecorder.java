package com.yk.media.record.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;

import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.RecordConfig;
import com.yk.media.record.listener.OnAudioRecordListener;

import java.io.IOException;
import java.nio.ByteBuffer;

public class AudioRecorder implements IAudioRecord {

    private RecordThread recordThread;

    private AudioEncodeConfig audioEncodeConfig;
    private RecordConfig recordConfig;

    private OnAudioRecordListener onAudioRecordListener;

    @Override
    public void prepare(String path) {
        AudioEncodeConfig audioEncodeConfig = AudioEncodeConfig.getDefault();

        RecordConfig recordConfig = RecordConfig.getDefault()
                .setPath(path);

        prepare(audioEncodeConfig, recordConfig);
    }

    @Override
    public void prepare(AudioEncodeConfig audioEncodeConfig, RecordConfig recordConfig) {
        cancelRecord();

        this.audioEncodeConfig = audioEncodeConfig;
        this.recordConfig = recordConfig;
    }

    @Override
    public void startRecord() {
        recordThread = new RecordThread(
                audioEncodeConfig,
                recordConfig
        );
        recordThread.setOnAudioRecordListener(onAudioRecordListener);
        recordThread.init();

        recordThread.start();
    }

    @Override
    public void stopRecord() {
        if (recordThread == null) {
            return;
        }

        recordThread.stopRecord();
        recordThread = null;
    }

    @Override
    public void cancelRecord() {
        if (recordThread == null) {
            return;
        }

        recordThread.cancelRecord();
        recordThread = null;
    }

    public void setOnAudioRecordListener(OnAudioRecordListener onAudioRecordListener) {
        this.onAudioRecordListener = onAudioRecordListener;
    }

    private static class RecordThread extends Thread {
        private MediaMuxer mediaMuxer;

        private AudioRecord audioRecord;
        private MediaCodec audioCodec;

        private final AudioEncodeConfig audioEncodeConfig;
        private final RecordConfig recordConfig;

        private int bufferSizeInBytes;

        private boolean isStopRecord = false;
        private boolean isCancelRecord = false;

        private OnAudioRecordListener onAudioRecordListener;

        public RecordThread(AudioEncodeConfig audioEncodeConfig, RecordConfig recordConfig) {
            this.audioEncodeConfig = audioEncodeConfig;
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

        @Override
        public void run() {
            super.run();
            record();
        }

        private void record() {
            if (mediaMuxer == null || audioCodec == null) {
                onRecordError(new IllegalArgumentException("widget is null"));
                return;
            }

            boolean isStartMuxer = false; // 合成是否开始

            isCancelRecord = false;
            isStopRecord = false;

            long audioPts = 0;

            int audioTrackIndex = -1;

            audioRecord.startRecording();
            audioCodec.start();

            onRecordStart(audioEncodeConfig, recordConfig);

            MediaCodec.BufferInfo audioInfo = new MediaCodec.BufferInfo();

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

                // 获取音频编码数据，写入Muxer
                int audioOutputBufferId = audioCodec.dequeueOutputBuffer(audioInfo, 0);
                if (audioOutputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    audioTrackIndex = mediaMuxer.addTrack(audioCodec.getOutputFormat());
                    isStartMuxer = true;
                    mediaMuxer.start();
                } else if (audioOutputBufferId >= 0) {
                    ByteBuffer outputBuffer = audioCodec.getOutputBuffer(audioOutputBufferId);
                    if (outputBuffer != null && audioInfo.size != 0 && isStartMuxer) {
                        outputBuffer.position(audioInfo.offset);
                        outputBuffer.limit(audioInfo.offset + audioInfo.size);
                        if (audioPts == 0) {
                            audioPts = audioInfo.presentationTimeUs;
                        }
                        audioInfo.presentationTimeUs = audioInfo.presentationTimeUs - audioPts;
                        onRecording(audioInfo.presentationTimeUs);
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

            if (mediaMuxer != null) {
                mediaMuxer.stop();
                mediaMuxer.release();
                mediaMuxer = null;
            }

            if (isCancelRecord) {
                onRecordCancel();
                return;
            }

            if (isStopRecord) {
                onRecordStop(recordConfig);
            }
        }

        private void onRecordStart(AudioEncodeConfig audioEncodeConfig, RecordConfig recordConfig) {
            if (onAudioRecordListener == null) {
                return;
            }
            onAudioRecordListener.onRecordStart(audioEncodeConfig, recordConfig);
        }

        private void onRecording(long pts) {
            if (onAudioRecordListener == null) {
                return;
            }
            onAudioRecordListener.onRecording(pts);
        }

        private void onRecordStop(RecordConfig recordConfig) {
            if (onAudioRecordListener == null) {
                return;
            }
            onAudioRecordListener.onRecordStop(recordConfig);
        }

        private void onRecordCancel() {
            if (onAudioRecordListener == null) {
                return;
            }
            onAudioRecordListener.onRecordCancel();
        }

        private void onRecordError(Exception e) {
            if (onAudioRecordListener == null) {
                return;
            }
            onAudioRecordListener.onRecordError(e);
        }

        public void setOnAudioRecordListener(OnAudioRecordListener onAudioRecordListener) {
            this.onAudioRecordListener = onAudioRecordListener;
        }
    }
}
