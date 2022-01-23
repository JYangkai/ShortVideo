package com.yk.shortvideo.ui.record;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.camera.CameraSize;
import com.yk.media.opengl.view.CameraView;
import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.RecordConfig;
import com.yk.media.record.config.VideoEncodeConfig;
import com.yk.media.record.listener.OnVideoRecordListener;
import com.yk.media.record.video.VideoRecorder;
import com.yk.media.source.Audio;
import com.yk.shortvideo.R;
import com.yk.shortvideo.ui.source.AudioListActivity;

import java.io.File;

public class CameraRecordActivity extends AppCompatActivity {
    private static final String TAG = "CameraRecordActivity";

    private static final int REQUEST_BGM_CODE = 1;

    private CameraView cameraView;
    private AppCompatButton btnBgm;
    private AppCompatButton btnSwitch;
    private AppCompatButton btnRecord;

    private boolean isRecord = false;

    private String path;

    private final VideoRecorder videoRecorder = new VideoRecorder();

    private Audio bgmAudio;

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
        btnBgm = findViewById(R.id.btnBgm);
        btnSwitch = findViewById(R.id.btnSwitch);
        btnRecord = findViewById(R.id.btnRecord);
    }

    private void initData() {
        path = getExternalFilesDir("mp4").getPath() + File.separator + System.currentTimeMillis() + ".mp4";
    }

    private void bindEvent() {
        videoRecorder.setOnVideoRecordListener(new OnVideoRecordListener() {
            @Override
            public void onRecordStart(AudioEncodeConfig audioEncodeConfig, VideoEncodeConfig videoEncodeConfig, RecordConfig recordConfig) {
                Log.d(TAG, "onRecordStart: ");
            }

            @Override
            public void onRecording(long pts) {

            }

            @Override
            public void onRecordStop(RecordConfig recordConfig) {
                Log.d(TAG, "onRecordStop: ");
            }

            @Override
            public void onRecordCancel() {
                Log.d(TAG, "onRecordCancel: ");
            }

            @Override
            public void onRecordError(Exception e) {
                Log.e(TAG, "onRecordError: ", e);
            }
        });

        btnBgm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioListActivity.startForResult(CameraRecordActivity.this, REQUEST_BGM_CODE);
            }
        });

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.switchCamera();
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecord) {
                    stopRecord();
                    btnRecord.setText("Record");
                } else {
                    startRecord();
                    btnRecord.setText("Stop");
                }
                isRecord = !isRecord;
            }
        });
    }

    private void startRecord() {
        CameraSize cameraSize = cameraView.getCameraConfig().getCameraSize();

        videoRecorder.prepare(
                this, cameraView.getEglContext(), cameraView.getFboTextureId(), true,
                cameraSize.getHeight(), cameraSize.getWidth(), path, bgmAudio != null ? bgmAudio.getPath() : null
        );
        videoRecorder.startRecord();
    }

    private void stopRecord() {
        videoRecorder.stopRecord();
    }

    private void cancelRecord() {
        videoRecorder.cancelRecord();
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
        cancelRecord();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BGM_CODE && resultCode == RESULT_OK && data != null) {
            bgmAudio = (Audio) data.getSerializableExtra(AudioListActivity.EXTRA_BACK_AUDIO);
            Toast.makeText(this, "添加BGM:" + bgmAudio.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
