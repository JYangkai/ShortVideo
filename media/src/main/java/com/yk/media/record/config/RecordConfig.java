package com.yk.media.record.config;

public class RecordConfig {
    private String path;
    private long maxTime;

    public static RecordConfig getDefault() {
        return new RecordConfig(null, 30000);
    }

    public RecordConfig(String path, long maxTime) {
        this.path = path;
        this.maxTime = maxTime;
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

    @Override
    public String toString() {
        return "RecordConfig{" +
                "path='" + path + '\'' +
                ", maxTime=" + maxTime +
                '}';
    }
}
