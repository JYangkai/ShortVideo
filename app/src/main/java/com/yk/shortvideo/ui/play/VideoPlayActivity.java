package com.yk.shortvideo.ui.play;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yk.media.opengl.view.VideoView;
import com.yk.shortvideo.R;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {

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
