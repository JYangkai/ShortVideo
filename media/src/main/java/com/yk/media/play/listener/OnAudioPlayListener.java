package com.yk.media.play.listener;

public interface OnAudioPlayListener {

    void onPlayStart(long duration);

    void onPlayPause();

    void onPlayContinue();

    void onPlayStop();

    void onPlayComplete();

    void onPlayError(Exception e);

}
