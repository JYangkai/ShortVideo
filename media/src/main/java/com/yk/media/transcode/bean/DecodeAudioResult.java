package com.yk.media.transcode.bean;

public class DecodeAudioResult {
    private String pcmPath;
    private int sampleRate;
    private int channelCount;
    private int bitRate;
    private int maxInputSize;

    public DecodeAudioResult(String pcmPath, int sampleRate, int channelCount, int bitRate, int maxInputSize) {
        this.pcmPath = pcmPath;
        this.sampleRate = sampleRate;
        this.channelCount = channelCount;
        this.bitRate = bitRate;
        this.maxInputSize = maxInputSize;
    }

    public String getPcmPath() {
        return pcmPath;
    }

    public void setPcmPath(String pcmPath) {
        this.pcmPath = pcmPath;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getMaxInputSize() {
        return maxInputSize;
    }

    public void setMaxInputSize(int maxInputSize) {
        this.maxInputSize = maxInputSize;
    }

    @Override
    public String toString() {
        return "DecodeResult{" +
                "pcmPath='" + pcmPath + '\'' +
                ", sampleRate=" + sampleRate +
                ", channelCount=" + channelCount +
                ", bitRate=" + bitRate +
                ", maxInputSize=" + maxInputSize +
                '}';
    }
}
