package com.yk.media.record.config;

import android.media.MediaRecorder;

public class MicConfig {
    private boolean enable;
    private int source;

    public static MicConfig getDefault() {
        return new MicConfig(true, MediaRecorder.AudioSource.MIC);
    }

    public MicConfig(boolean enable, int source) {
        this.enable = enable;
        this.source = source;
    }

    public boolean isEnable() {
        return enable;
    }

    public MicConfig setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }

    public int getSource() {
        return source;
    }

    public MicConfig setSource(int source) {
        this.source = source;
        return this;
    }

    @Override
    public String toString() {
        return "MicConfig{" +
                "enable=" + enable +
                ", source=" + source +
                '}';
    }
}
