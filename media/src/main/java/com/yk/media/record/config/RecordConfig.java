package com.yk.media.record.config;

public class RecordConfig {
    private String path;
    private long maxTime;
    private boolean enableAudio;
    private String bgmPath;

    public static RecordConfig getDefault() {
        return new RecordConfig(null, 30000, true, null);
    }

    public RecordConfig(String path, long maxTime, boolean enableAudio, String bgmPath) {
        this.path = path;
        this.maxTime = maxTime;
        this.enableAudio = enableAudio;
        this.bgmPath = bgmPath;
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

    public String getBgmPath() {
        return bgmPath;
    }

    public RecordConfig setBgmPath(String bgmPath) {
        this.bgmPath = bgmPath;
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
