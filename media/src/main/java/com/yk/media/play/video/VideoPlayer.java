package com.yk.media.play.video;

import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.yk.media.play.listener.OnVideoPlayListener;

import java.io.IOException;

public class VideoPlayer implements IVideoPlay {

    private MediaPlayer mediaPlayer;

    private OnVideoPlayListener onVideoPlayListener;

    @Override
    public void play(String path, Object surface) {
        play(path, surface, true);
    }

    @Override
    public void play(String path, Object surface, boolean isLoop) {
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
                    onPlayStart(mp.getVideoWidth(), mp.getVideoHeight(), mp.getDuration());
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
            if (surface instanceof SurfaceHolder) {
                mediaPlayer.setDisplay((SurfaceHolder) surface);
            } else if (surface instanceof Surface) {
                mediaPlayer.setSurface((Surface) surface);
            }
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

    @Override
    public int getWidth() {
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getVideoWidth();
    }

    @Override
    public int getHeight() {
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getVideoHeight();
    }

    private void onPlayStart(int width, int height, long duration) {
        if (onVideoPlayListener == null) {
            return;
        }
        onVideoPlayListener.onPlayStart(width, height, duration);
    }

    private void onPlayPause() {
        if (onVideoPlayListener == null) {
            return;
        }
        onVideoPlayListener.onPlayPause();
    }

    private void onPlayContinue() {
        if (onVideoPlayListener == null) {
            return;
        }
        onVideoPlayListener.onPlayContinue();
    }

    private void onPlayStop() {
        if (onVideoPlayListener == null) {
            return;
        }
        onVideoPlayListener.onPlayStop();
    }

    private void onPlayComplete() {
        if (onVideoPlayListener == null) {
            return;
        }
        onVideoPlayListener.onPlayComplete();
    }

    private void onPlayError(Exception e) {
        if (onVideoPlayListener == null) {
            return;
        }
        onVideoPlayListener.onPlayError(e);
    }

    public void setOnVideoPlayListener(OnVideoPlayListener onVideoPlayListener) {
        this.onVideoPlayListener = onVideoPlayListener;
    }
}
