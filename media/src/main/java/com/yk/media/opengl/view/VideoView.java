package com.yk.media.opengl.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Surface;

import com.yk.media.opengl.render.apply.VideoRender;
import com.yk.media.opengl.render.core.OesCallbackRender;
import com.yk.media.play.listener.OnVideoPlayListener;
import com.yk.media.play.video.VideoPlayer;

public class VideoView extends GLSurfaceView implements OnVideoPlayListener {

    private final VideoPlayer videoPlayer = new VideoPlayer();
    private final VideoRender render;

    public VideoView(Context context) {
        this(context, null);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        render = new VideoRender(context);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        videoPlayer.setOnVideoPlayListener(this);
    }

    public void startPlay(String path) {
        render.setOnOesListener(new OesCallbackRender.OnOesListener() {
            @Override
            public void onOes(SurfaceTexture surfaceTexture) {
                surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                    @Override
                    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                        requestRender();
                    }
                });
                videoPlayer.play(path, new Surface(surfaceTexture));
            }
        });
    }

    public void pausePlay() {
        videoPlayer.pause();
    }

    public void continuePlay() {
        videoPlayer.continuePlay();
    }

    public void seekTo(int time) {
        videoPlayer.seekTo(time);
    }

    public void stopPlay() {
        videoPlayer.stop();
    }

    @Override
    public void onPlayStart(int width, int height, long duration) {
        render.setOesSize(width, height);
        requestRender();
    }

    @Override
    public void onPlayPause() {

    }

    @Override
    public void onPlayContinue() {

    }

    @Override
    public void onPlayStop() {

    }

    @Override
    public void onPlayComplete() {

    }

    @Override
    public void onPlayError(Exception e) {
        stopPlay();
    }
}
