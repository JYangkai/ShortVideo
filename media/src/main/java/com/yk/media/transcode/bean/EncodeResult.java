package com.yk.media.transcode.bean;

public class EncodeResult {
    private String aacPath;

    public EncodeResult(String aacPath) {
        this.aacPath = aacPath;
    }

    public String getAacPath() {
        return aacPath;
    }

    public void setAacPath(String aacPath) {
        this.aacPath = aacPath;
    }

    @Override
    public String toString() {
        return "EncodeResult{" +
                "aacPath='" + aacPath + '\'' +
                '}';
    }
}
