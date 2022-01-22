package com.yk.media.play.audio;

import android.media.MediaPlayer;
import android.text.TextUtils;

import com.yk.media.play.listener.OnAudioPlayListener;

import java.io.IOException;

public class AudioPlayer implements IAudioPlay {

    private MediaPlayer mediaPlayer;

    private OnAudioPlayListener onAudioPlayListener;

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
        if (onAudioPlayListener == null) {
            return;
        }
        onAudioPlayListener.onPlayStart(duration);
    }

    private void onPlayPause() {
        if (onAudioPlayListener == null) {
            return;
        }
        onAudioPlayListener.onPlayPause();
    }

    private void onPlayContinue() {
        if (onAudioPlayListener == null) {
            return;
        }
        onAudioPlayListener.onPlayContinue();
    }

    private void onPlayStop() {
        if (onAudioPlayListener == null) {
            return;
        }
        onAudioPlayListener.onPlayStop();
    }

    private void onPlayComplete() {
        if (onAudioPlayListener == null) {
            return;
        }
        onAudioPlayListener.onPlayComplete();
    }

    private void onPlayError(Exception e) {
        if (onAudioPlayListener == null) {
            return;
        }
        onAudioPlayListener.onPlayError(e);
    }

    public void setOnAudioPlayListener(OnAudioPlayListener onAudioPlayListener) {
        this.onAudioPlayListener = onAudioPlayListener;
    }
}
