package com.yk.media.transcode;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class AudioTranscoder {
    private static final String TAG = "AudioTranscoder";

    private TranscodeThread transcodeThread;

    public void start(String inputPath, String outputPath) {
        cancel();

        transcodeThread = new TranscodeThread(inputPath, outputPath);
        transcodeThread.start();
    }

    public void stop() {
        if (transcodeThread == null) {
            return;
        }
        transcodeThread.stopTranscode();
        transcodeThread = null;
    }

    public void cancel() {
        if (transcodeThread == null) {
            return;
        }
        transcodeThread.cancelTranscode();
        transcodeThread = null;
    }

    private static class TranscodeThread extends Thread {
        private final String inputPath;
        private final String outputPath;

        private MediaExtractor extractor;
        private MediaMuxer muxer;

        private MediaCodec deCodec;
        private MediaCodec enCodec;

        private boolean isStopTranscode = false;
        private boolean isCancelTranscode = false;

        public TranscodeThread(String inputPath, String outputPath) {
            this.inputPath = inputPath;
            this.outputPath = outputPath;
        }

        public void stopTranscode() {
            isStopTranscode = true;
            try {
                join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void cancelTranscode() {
            isCancelTranscode = true;
            try {
                join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            initExtractor();
            initMuxer();
//            transCode();
        }

        private void initExtractor() {
            try {
                extractor = new MediaExtractor();
                extractor.setDataSource(inputPath);

                int trackCount = extractor.getTrackCount();
                for (int i = 0; i < trackCount; i++) {
                    MediaFormat format = extractor.getTrackFormat(i);
                    String mime = format.getString(MediaFormat.KEY_MIME);
                    if (!TextUtils.isEmpty(mime) && mime.startsWith("audio/")) {
                        initDeCodec(format);
                        initEnCodec(format);
                        extractor.selectTrack(i);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                extractor = null;
            }
        }

        private void initMuxer() {
            try {
                muxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            } catch (IOException e) {
                e.printStackTrace();
                muxer = null;
            }
        }

        private void initDeCodec(MediaFormat format) {
            Log.d(TAG, "initDeCodec: de format:" + format);
            String mime = format.getString(MediaFormat.KEY_MIME);
            try {
                deCodec = MediaCodec.createDecoderByType(mime);
                deCodec.configure(format, null, null, 0);
            } catch (IOException e) {
                e.printStackTrace();
                deCodec = null;
            }
        }

        private void initEnCodec(MediaFormat format) {
            int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            int channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            int bitRate = format.getInteger(MediaFormat.KEY_BIT_RATE);
            int maxInputSize = format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);

            MediaFormat encodeFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, sampleRate, channelCount);
            encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
            encodeFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, maxInputSize);

            Log.d(TAG, "initEnCodec: en format:" + encodeFormat);

            try {
                enCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
                enCodec.configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            } catch (IOException e) {
                e.printStackTrace();
                enCodec = null;
            }
        }

        private void transCode() {
            if (extractor == null || muxer == null || deCodec == null || enCodec == null) {
                return;
            }

            boolean isStartMuxer = false;
            isCancelTranscode = false;
            isStopTranscode = false;

            long audioPts = 0;

            int audioTrackIndex = -1;

            Log.d(TAG, "transCode: start");

            deCodec.start();
            enCodec.start();

            MediaCodec.BufferInfo deInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo enInfo = new MediaCodec.BufferInfo();

            for (; ; ) {
                if (isStopTranscode || isCancelTranscode) {
                    release();
                    break;
                }

                int deAudioInputBufferId = deCodec.dequeueInputBuffer(0);
                if (deAudioInputBufferId >= 0) {
                    ByteBuffer inputBuffer = deCodec.getInputBuffer(deAudioInputBufferId);
                    int readSize = -1;
                    if (inputBuffer != null) {
                        readSize = extractor.readSampleData(inputBuffer, 0);
                    }
                    if (readSize > 0) {
                        Log.d(TAG, "transCode: queue to decode");
                        deCodec.queueInputBuffer(deAudioInputBufferId, 0, readSize, extractor.getSampleTime(), 0);
                        extractor.advance();
                    } else {
                        isStopTranscode = true;
                    }
                }

                byte[] deData = null;

                int deAudioOutputBufferId = deCodec.dequeueOutputBuffer(deInfo, 0);
                if (deAudioOutputBufferId >= 0) {
                    ByteBuffer outputBuffer = deCodec.getOutputBuffer(deAudioOutputBufferId);
                    if (outputBuffer != null && deInfo.size > 0) {
                        deData = new byte[deInfo.size];
                        outputBuffer.get(deData);
                    }
                    Log.d(TAG, "transCode: release to decode");
                    deCodec.releaseOutputBuffer(deAudioOutputBufferId, false);
                }

                while (deData != null) {
                    int enAudioInputBufferId = enCodec.dequeueInputBuffer(0);
                    if (enAudioInputBufferId >= 0) {
                        ByteBuffer inputBuffer = enCodec.getInputBuffer(enAudioInputBufferId);
                        int readSize = -1;
                        if (inputBuffer != null) {
                            int capacity = inputBuffer.capacity();
                            if (capacity < deData.length) {
                                int remainder = deData.length - capacity;
                                byte[] curData = new byte[capacity];
                                byte[] remainData = new byte[remainder];
                                System.arraycopy(deData, 0, curData, 0, capacity);
                                System.arraycopy(deData, capacity, remainData, 0, remainder);

                                deData = remainData;

                                readSize = capacity;
                                inputBuffer.put(curData);

                                Log.d(TAG, "transCode: put to cur");
                            } else {
                                readSize = deData.length;
                                inputBuffer.put(deData);
                                deData = null;

                                Log.d(TAG, "transCode: put to deData");
                            }
                        }
                        if (readSize > 0) {
                            Log.d(TAG, "transCode: queue to encode");
                            enCodec.queueInputBuffer(enAudioInputBufferId, 0, readSize, System.nanoTime() / 1000, 0);
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }

                int enAudioOutputBufferId = enCodec.dequeueOutputBuffer(enInfo, 0);
                if (enAudioOutputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    audioTrackIndex = muxer.addTrack(enCodec.getOutputFormat());
                    isStartMuxer = true;
                    muxer.start();
                    Log.d(TAG, "transCode: start to muxer");
                } else if (enAudioOutputBufferId >= 0) {
                    ByteBuffer outputBuffer = enCodec.getOutputBuffer(enAudioOutputBufferId);
                    if (outputBuffer != null && enInfo.size > 0 && isStartMuxer) {
                        outputBuffer.position(enInfo.offset);
                        outputBuffer.limit(enInfo.offset + enInfo.size);
                        if (audioPts == 0) {
                            audioPts = enInfo.presentationTimeUs;
                        }
                        enInfo.presentationTimeUs = enInfo.presentationTimeUs - audioPts;
                        muxer.writeSampleData(audioTrackIndex, outputBuffer, enInfo);
                        Log.d(TAG, "transCode: write to muxer");
                    }
                    enCodec.releaseOutputBuffer(enAudioOutputBufferId, false);
                    Log.d(TAG, "transCode: release to encode");
                }
            }
        }

        private void release() {
            if (deCodec != null) {
                deCodec.stop();
                deCodec.release();
                deCodec = null;
            }

            if (enCodec != null) {
                enCodec.stop();
                enCodec.release();
                enCodec = null;
            }

            if (extractor != null) {
                extractor.release();
                extractor = null;
            }

            if (muxer != null) {
                muxer.stop();
                muxer.release();
                muxer = null;
            }

            Log.d(TAG, "release: " + outputPath);
        }
    }

}
