package com.yk.media.play.audio;

public interface IAudioPlay {

    void play(String path);

    void play(String path, boolean isLoop);

    void pause();

    void continuePlay();

    void seekTo(int time);

    void stop();

    long getDuration();

    long getCurPosition();

    boolean isPlaying();

}
