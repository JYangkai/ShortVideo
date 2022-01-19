package com.yk.media.record.config;

import android.media.MediaFormat;

public class VideoEncodeConfig {
    private int width;
    private int height;
    private int frameRate;
    private int iFrameInterval;
    private String mime;

    public static VideoEncodeConfig getDefault() {
        return new VideoEncodeConfig(
                -1, -1,
                30, 1,
                MediaFormat.MIMETYPE_VIDEO_AVC
        );
    }

    public VideoEncodeConfig(int width, int height, int frameRate, int iFrameInterval, String mime) {
        this.width = width;
        this.height = height;
        this.frameRate = frameRate;
        this.iFrameInterval = iFrameInterval;
        this.mime = mime;
    }

    public int getWidth() {
        return width;
    }

    public VideoEncodeConfig setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public VideoEncodeConfig setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public VideoEncodeConfig setFrameRate(int frameRate) {
        this.frameRate = frameRate;
        return this;
    }

    public int getiFrameInterval() {
        return iFrameInterval;
    }

    public VideoEncodeConfig setiFrameInterval(int iFrameInterval) {
        this.iFrameInterval = iFrameInterval;
        return this;
    }

    public String getMime() {
        return mime;
    }

    public VideoEncodeConfig setMime(String mime) {
        this.mime = mime;
        return this;
    }

    @Override
    public String toString() {
        return "VideoEncodeConfig{" +
                "width=" + width +
                ", height=" + height +
                ", frameRate=" + frameRate +
                ", iFrameInterval=" + iFrameInterval +
                ", mime='" + mime + '\'' +
                '}';
    }
}
