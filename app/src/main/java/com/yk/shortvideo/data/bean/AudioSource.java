package com.yk.shortvideo.data.bean;

import androidx.annotation.Nullable;

import com.yk.media.source.Audio;

public class AudioSource {
    public enum State {
        UNKNOWN,
        CAN_USE,
        NEED_TRANSCODE
    }

    private Audio audio;
    private State state = State.UNKNOWN;

    public AudioSource(Audio audio) {
        this.audio = audio;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AudioSource{" +
                "audio=" + audio +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof AudioSource) {
            AudioSource audioSource = (AudioSource) obj;

            String name1 = audioSource.getAudio().getName();
            name1 = name1.substring(0, name1.lastIndexOf("."));

            String name2 = this.getAudio().getName();
            name2 = name2.substring(0, name2.lastIndexOf("."));

            return name1.equals(name2);
        }
        return super.equals(obj);
    }
}
