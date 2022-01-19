package com.yk.media.record.config;

import android.media.AudioFormat;
import android.media.MediaFormat;

public class AudioEncodeConfig {
    private int sampleRate;
    private int bitRate;
    private int maxInputSize;
    private int channelConfig;
    private int audioFormat;
    private String mime;

    public static AudioEncodeConfig getDefault() {
        return new AudioEncodeConfig(
                96000,
                44100,
                4096,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                MediaFormat.MIMETYPE_AUDIO_AAC
        );
    }

    public AudioEncodeConfig(int sampleRate, int bitRate, int maxInputSize, int channelConfig, int audioFormat, String mime) {
        this.sampleRate = sampleRate;
        this.bitRate = bitRate;
        this.maxInputSize = maxInputSize;
        this.channelConfig = channelConfig;
        this.audioFormat = audioFormat;
        this.mime = mime;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public AudioEncodeConfig setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public int getBitRate() {
        return bitRate;
    }

    public AudioEncodeConfig setBitRate(int bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    public int getMaxInputSize() {
        return maxInputSize;
    }

    public AudioEncodeConfig setMaxInputSize(int maxInputSize) {
        this.maxInputSize = maxInputSize;
        return this;
    }

    public int getChannelConfig() {
        return channelConfig;
    }

    public AudioEncodeConfig setChannelConfig(int channelConfig) {
        this.channelConfig = channelConfig;
        return this;
    }

    public int getAudioFormat() {
        return audioFormat;
    }

    public AudioEncodeConfig setAudioFormat(int audioFormat) {
        this.audioFormat = audioFormat;
        return this;
    }

    public String getMime() {
        return mime;
    }

    public AudioEncodeConfig setMime(String mime) {
        this.mime = mime;
        return this;
    }

    @Override
    public String toString() {
        return "AudioEncodeConfig{" +
                "sampleRate=" + sampleRate +
                ", bitRate=" + bitRate +
                ", maxInputSize=" + maxInputSize +
                ", channelConfig=" + channelConfig +
                ", audioFormat=" + audioFormat +
                ", mime='" + mime + '\'' +
                '}';
    }
}
