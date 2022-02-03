package com.yk.shortvideo.data.event;

public class UseBGMEvent {

    private String path;

    public UseBGMEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "UserBGMEvent{" +
                "path='" + path + '\'' +
                '}';
    }
}
