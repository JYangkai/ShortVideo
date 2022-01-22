package com.yk.media.play.listener;

public interface OnVideoPlayListener {

    void onPlayStart(int width, int height, long duration);

    void onPlayPause();

    void onPlayContinue();

    void onPlayStop();

    void onPlayComplete();

    void onPlayError(Exception e);

}
