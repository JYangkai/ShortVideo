package com.yk.media.play.audio;

import android.media.MediaPlayer;
import android.text.TextUtils;

import com.yk.media.play.listener.OnAudioPlayListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioPlayer implements IAudioPlay {

    private MediaPlayer mediaPlayer;

    private final List<OnAudioPlayListener> onAudioPlayListenerList = new ArrayList<>();

    @Override
    public void play(String path) {
        play(path, true);
    }

    @Override
    public void play(String path, boolean isLoop) {
        if (TextUtils.isEmpty(path)) {
            onPlayError(new IllegalArgumentException("path is null"));
            return;
        }

        stop();

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mp == null) {
                        return;
                    }
                    mp.start();
                    onPlayStart(mp.getDuration());
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    onPlayError(new RuntimeException("what:" + what + " extra:" + extra));
                    return true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onPlayComplete();
                }
            });
            mediaPlayer.setLooping(isLoop);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.pause();
        onPlayPause();
    }

    @Override
    public void continuePlay() {
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.start();
        onPlayContinue();
    }

    @Override
    public void seekTo(int time) {
        if (mediaPlayer == null) {
            return;
        }
        if (time >= getDuration()) {
            return;
        }
        mediaPlayer.seekTo(time);
    }

    @Override
    public void stop() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        onPlayStop();
    }

    @Override
    public long getDuration() {
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getDuration();
    }

    @Override
    public long getCurPosition() {
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    private void onPlayStart(long duration) {
        if (onAudioPlayListenerList.isEmpty()) {
            return;
        }
        for (OnAudioPlayListener onAudioPlayListener : onAudioPlayListenerList) {
            onAudioPlayListener.onPlayStart(duration);
        }
    }

    private void onPlayPause() {
        if (onAudioPlayListenerList.isEmpty()) {
            return;
        }
        for (OnAudioPlayListener onAudioPlayListener : onAudioPlayListenerList) {
            onAudioPlayListener.onPlayPause();
        }
    }

    private void onPlayContinue() {
        if (onAudioPlayListenerList.isEmpty()) {
            return;
        }
        for (OnAudioPlayListener onAudioPlayListener : onAudioPlayListenerList) {
            onAudioPlayListener.onPlayContinue();
        }
    }

    private void onPlayStop() {
        if (onAudioPlayListenerList.isEmpty()) {
            return;
        }
        for (OnAudioPlayListener onAudioPlayListener : onAudioPlayListenerList) {
            onAudioPlayListener.onPlayStop();
        }
    }

    private void onPlayComplete() {
        if (onAudioPlayListenerList.isEmpty()) {
            return;
        }
        for (OnAudioPlayListener onAudioPlayListener : onAudioPlayListenerList) {
            onAudioPlayListener.onPlayComplete();
        }
    }

    private void onPlayError(Exception e) {
        if (onAudioPlayListenerList.isEmpty()) {
            return;
        }
        for (OnAudioPlayListener onAudioPlayListener : onAudioPlayListenerList) {
            onAudioPlayListener.onPlayError(e);
        }
    }

    public void addOnAudioPlayListener(OnAudioPlayListener onAudioPlayListener) {
        onAudioPlayListenerList.add(onAudioPlayListener);
    }

    public void setOnAudioPlayListener(OnAudioPlayListener onAudioPlayListener) {
        onAudioPlayListenerList.clear();
        onAudioPlayListenerList.add(onAudioPlayListener);
    }
}
