package com.yk.media.source;

import java.io.Serializable;

public class Audio implements Serializable {

    private String name;
    private String path;
    private long duration;

    public Audio(String name, String path, long duration) {
        this.name = name;
        this.path = path;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Audio{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                '}';
    }
}
