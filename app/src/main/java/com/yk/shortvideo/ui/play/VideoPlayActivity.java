package com.yk.shortvideo.ui.play;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yk.media.opengl.view.VideoView;
import com.yk.media.play.listener.OnVideoPlayListener;
import com.yk.shortvideo.R;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {
    private static final String TAG = "VideoPlayActivity";

    private VideoView videoView;

    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        videoView = findViewById(R.id.videoView);
    }

    private void initData() {
        path = getExternalFilesDir("mp4") + File.separator + "1642826549733.mp4";
    }

    private void bindEvent() {
        videoView.addOnVideoPlayListener(new OnVideoPlayListener() {
            @Override
            public void onPlayStart(int width, int height, long duration) {
                Log.d(TAG, "onPlayStart: width:" + width + " height:" + height + " duration:" + duration);
            }

            @Override
            public void onPlayPause() {
                Log.d(TAG, "onPlayPause: ");
            }

            @Override
            public void onPlayContinue() {
                Log.d(TAG, "onPlayContinue: ");
            }

            @Override
            public void onPlayStop() {
                Log.d(TAG, "onPlayStop: ");
            }

            @Override
            public void onPlayComplete() {
                Log.d(TAG, "onPlayComplete: ");
            }

            @Override
            public void onPlayError(Exception e) {
                Log.e(TAG, "onPlayError: ", e);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.startPlay(path);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.stopPlay();
    }
}
