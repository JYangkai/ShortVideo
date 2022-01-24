package com.yk.media.transcode.audio;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;

import com.yk.media.transcode.bean.DecodeResult;
import com.yk.media.transcode.bean.EncodeResult;
import com.yk.media.transcode.listener.OnAudioTranscodeListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class AudioTranscoder {
    private TranscodeThread transcodeThread;

    private OnAudioTranscodeListener onAudioTranscodeListener;

    public void start(String inputPath, String outputPath) {
        cancel();

        transcodeThread = new TranscodeThread(inputPath, outputPath);
        transcodeThread.setOnAudioTranscodeListener(onAudioTranscodeListener);
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

    public void setOnAudioTranscodeListener(OnAudioTranscodeListener onAudioTranscodeListener) {
        this.onAudioTranscodeListener = onAudioTranscodeListener;
    }

    private static class TranscodeThread extends Thread {
        private final String inputPath;
        private final String outputPath;

        private boolean isStopTranscode = false;
        private boolean isCancelTranscode = false;

        private OnAudioTranscodeListener onAudioTranscodeListener;

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
            try {
                onAudioTranscodeStart();

                DecodeResult decodeResult = decode();
                if (decodeResult == null) {
                    onAudioTranscodeError(new RuntimeException("decode is fail"));
                    return;
                }

                EncodeResult encodeResult = encode(decodeResult);

                if (isCancelTranscode) {
                    onAudioTranscodeCancel();
                    return;
                }

                onAudioTranscodeStop(decodeResult, encodeResult);
            } catch (IOException e) {
                e.printStackTrace();
                onAudioTranscodeError(e);
            }
        }

        private DecodeResult decode() throws IOException {
            MediaExtractor extractor = new MediaExtractor();
            extractor.setDataSource(inputPath);

            MediaFormat format = null;
            String mime = null;

            int trackCount = extractor.getTrackCount();
            for (int i = 0; i < trackCount; i++) {
                format = extractor.getTrackFormat(i);
                mime = format.getString(MediaFormat.KEY_MIME);
                if (!TextUtils.isEmpty(mime) && mime.startsWith("audio/")) {
                    extractor.selectTrack(i);
                    break;
                }
            }

            if (format == null || mime == null) {
                return null;
            }

            MediaCodec mediaCodec = MediaCodec.createDecoderByType(mime);
            mediaCodec.configure(format, null, null, 0);
            mediaCodec.start();

            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            String pcmPath = outputPath.substring(0, outputPath.lastIndexOf(File.separator) + 1) + System.currentTimeMillis() + ".pcm";
            FileOutputStream fos = new FileOutputStream(pcmPath);

            boolean isDecodeEnd = false;
            while (!isDecodeEnd && !isStopTranscode && !isCancelTranscode) {
                int inputBufferId = mediaCodec.dequeueInputBuffer(0);
                if (inputBufferId >= 0) {
                    ByteBuffer inputBuffer = mediaCodec.getInputBuffer(inputBufferId);
                    int readSize = -1;
                    if (inputBuffer != null) {
                        readSize = extractor.readSampleData(inputBuffer, 0);
                    }
                    if (readSize > 0) {
                        mediaCodec.queueInputBuffer(inputBufferId, 0, readSize, extractor.getSampleTime(), 0);
                        extractor.advance();
                    } else {
                        mediaCodec.queueInputBuffer(inputBufferId, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        isDecodeEnd = true;
                    }
                }

                int outputBufferId = mediaCodec.dequeueOutputBuffer(info, 0);
                while (outputBufferId >= 0) {
                    ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(outputBufferId);
                    if (outputBuffer != null && info.size > 0) {
                        byte[] data = new byte[info.size];
                        outputBuffer.get(data);
                        fos.write(data);
                    }
                    mediaCodec.releaseOutputBuffer(outputBufferId, false);
                    outputBufferId = mediaCodec.dequeueOutputBuffer(info, 0);
                }
            }
            extractor.release();
            mediaCodec.stop();
            mediaCodec.release();
            fos.close();

            int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            int channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            int bitRate = format.getInteger(MediaFormat.KEY_BIT_RATE);
            int maxInputSize = format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);

            return new DecodeResult(pcmPath, sampleRate, channelCount, bitRate, maxInputSize);
        }

        private EncodeResult encode(DecodeResult decodeResult) throws IOException {
            MediaFormat format = MediaFormat.createAudioFormat(
                    MediaFormat.MIMETYPE_AUDIO_AAC,
                    decodeResult.getSampleRate(),
                    decodeResult.getChannelCount()
            );
            format.setInteger(MediaFormat.KEY_BIT_RATE, decodeResult.getBitRate());
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, decodeResult.getMaxInputSize());
            format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);

            MediaCodec mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();

            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            FileInputStream pcmFis = new FileInputStream(decodeResult.getPcmPath());
            FileOutputStream aacFos = new FileOutputStream(outputPath);

            boolean isEncodeEnd = false;
            while (!isEncodeEnd && !isStopTranscode && !isCancelTranscode) {
                int inputBufferId = mediaCodec.dequeueInputBuffer(0);
                if (inputBufferId >= 0) {
                    ByteBuffer inputBuffer = mediaCodec.getInputBuffer(inputBufferId);
                    int readSize = -1;
                    if (inputBuffer != null) {
                        int len = inputBuffer.limit() - inputBuffer.position();
                        byte[] data = new byte[len];
                        readSize = pcmFis.read(data);
                        inputBuffer.put(data);
                    }
                    if (readSize > 0) {
                        mediaCodec.queueInputBuffer(inputBufferId, 0, readSize, System.nanoTime() / 1000, 0);
                    } else {
                        mediaCodec.queueInputBuffer(inputBufferId, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        isEncodeEnd = true;
                    }
                }

                int outputBufferId = mediaCodec.dequeueOutputBuffer(info, 0);
                while (outputBufferId >= 0) {
                    ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(outputBufferId);
                    if (outputBuffer != null && info.size > 0) {
                        byte[] data = new byte[info.size + 7];
                        addADTSHeader(data, info.size + 7);
                        outputBuffer.get(data, 7, info.size);
                        aacFos.write(data);
                    }
                    mediaCodec.releaseOutputBuffer(outputBufferId, false);
                    outputBufferId = mediaCodec.dequeueOutputBuffer(info, 0);
                }
            }
            mediaCodec.stop();
            mediaCodec.release();
            pcmFis.close();
            aacFos.close();

            File file = new File(decodeResult.getPcmPath());
            if (file.exists()) {
                file.delete();
            }

            return new EncodeResult(outputPath);
        }

        private void addADTSHeader(byte[] packet, int packetLen) {
            int profile = 2; // AAC
            int freqIdx = 4; // 44.1kHz
            int chanCfg = 2; // Channel Stereo
            packet[0] = (byte) 0xFF;
            packet[1] = (byte) 0xF9;
            packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
            packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
            packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
            packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
            packet[6] = (byte) 0xFC;
        }

        private void onAudioTranscodeStart() {
            if (onAudioTranscodeListener == null) {
                return;
            }
            onAudioTranscodeListener.onAudioTranscodeStart();
        }

        private void onAudioTranscoding(long progress) {
            if (onAudioTranscodeListener == null) {
                return;
            }
            onAudioTranscodeListener.onAudioTranscoding(progress);
        }

        private void onAudioTranscodeStop(DecodeResult decodeResult, EncodeResult encodeResult) {
            if (onAudioTranscodeListener == null) {
                return;
            }
            onAudioTranscodeListener.onAudioTranscodeStop(decodeResult, encodeResult);
        }

        private void onAudioTranscodeCancel() {
            if (onAudioTranscodeListener == null) {
                return;
            }
            onAudioTranscodeListener.onAudioTranscodeCancel();
        }

        private void onAudioTranscodeError(Exception e) {
            if (onAudioTranscodeListener == null) {
                return;
            }
            onAudioTranscodeListener.onAudioTranscodeError(e);
        }

        public void setOnAudioTranscodeListener(OnAudioTranscodeListener onAudioTranscodeListener) {
            this.onAudioTranscodeListener = onAudioTranscodeListener;
        }
    }

}
