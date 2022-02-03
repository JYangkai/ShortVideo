package com.yk.shortvideo.ui.play.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.yk.media.opengl.view.VideoView;
import com.yk.mvp.BaseMvpActivity;
import com.yk.shortvideo.R;

public class VideoPlayActivity extends BaseMvpActivity<IVideoPlayView, VideoPlayPresenter> implements IVideoPlayView {
    private static final String TAG = "VideoPlayActivity";

    private static final String EXTRA_PATH = "extra_path";

    private VideoView videoView;

    private String path;

    public static void start(Context context, String path) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(EXTRA_PATH, path);
        context.startActivity(intent);
    }

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
        path = getIntent().getStringExtra(EXTRA_PATH);
        Log.d(TAG, "initData: " + path);
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

    @Override
    public VideoPlayPresenter createPresenter() {
        return new VideoPlayPresenter();
    }
}
