package com.yk.media.record.config;

public class RecordConfig {
    private String path;
    private long maxTime;
    private boolean enableAudio;

    public static RecordConfig getDefault() {
        return new RecordConfig(null, 30000, true);
    }

    public RecordConfig(String path, long maxTime, boolean enableAudio) {
        this.path = path;
        this.maxTime = maxTime;
        this.enableAudio = enableAudio;
    }

    public String getPath() {
        return path;
    }

    public RecordConfig setPath(String path) {
        this.path = path;
        return this;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public RecordConfig setMaxTime(long maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public boolean isEnableAudio() {
        return enableAudio;
    }

    public RecordConfig setEnableAudio(boolean enableAudio) {
        this.enableAudio = enableAudio;
        return this;
    }

    @Override
    public String toString() {
        return "RecordConfig{" +
                "path='" + path + '\'' +
                ", maxTime=" + maxTime +
                ", enableAudio=" + enableAudio +
                '}';
    }
}
