package com.yk.media.play.video;

public interface IVideoPlay {

    void play(String path, Object surface);

    void play(String path, Object surface, boolean isLoop);

    void pause();

    void continuePlay();

    void seekTo(int time);

    void stop();

    long getDuration();

    long getCurPosition();

    boolean isPlaying();

    int getWidth();

    int getHeight();

}
