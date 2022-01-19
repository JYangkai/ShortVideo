package com.yk.shortvideo.ui.record;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yk.media.opengl.view.CameraView;
import com.yk.shortvideo.R;

public class CameraRecordActivity extends AppCompatActivity {

    private CameraView cameraView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_record);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        cameraView = findViewById(R.id.cameraView);
    }

    private void initData() {

    }

    private void bindEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.closeCamera();
    }
}
