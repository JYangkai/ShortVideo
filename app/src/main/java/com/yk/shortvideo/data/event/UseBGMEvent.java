package com.yk.shortvideo.data.event;

import com.yk.shortvideo.data.bean.AudioSource;

public class UseBGMEvent {

    private AudioSource source;

    public UseBGMEvent(AudioSource source) {
        this.source = source;
    }

    public AudioSource getSource() {
        return source;
    }

    public void setSource(AudioSource source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "UseBGMEvent{" +
                "source=" + source +
                '}';
    }
}
