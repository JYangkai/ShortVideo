package com.yk.shortvideo.ui.record.video;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.opengl.view.CameraView;
import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.RecordConfig;
import com.yk.media.record.config.VideoEncodeConfig;
import com.yk.media.record.listener.OnVideoRecordListener;
import com.yk.mvp.BaseMvpActivity;
import com.yk.shortvideo.R;

public class RecordVideoActivity extends BaseMvpActivity<IRecordVideoView, RecordVideoPresenter> implements IRecordVideoView {
    private static final String TAG = "RecordVideoActivity";

    private CameraView cameraView;
    private AppCompatButton btnRecord;
    private AppCompatButton btnSwitch;
    private AppCompatButton btnTools;
    private AppCompatButton btnBGM;

    private String bgmPath;

    private boolean isRecord = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        cameraView = findViewById(R.id.cameraView);
        btnRecord = findViewById(R.id.btnRecord);
        btnSwitch = findViewById(R.id.btnSwitch);
        btnTools = findViewById(R.id.btnTools);
        btnBGM = findViewById(R.id.btnBGM);
    }

    private void initData() {

    }

    private void bindEvent() {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecord) {
                    presenter.stopRecord();
                    btnRecord.setText("录制");
                } else {
                    presenter.startRecord(
                            cameraView.getEglContext(),
                            cameraView.getFboTextureId(),
                            true,
                            cameraView.getCameraConfig().getCameraSize().getHeight(),
                            cameraView.getCameraConfig().getCameraSize().getWidth(),
                            bgmPath
                    );
                    btnRecord.setText("停止");
                }
                isRecord = !isRecord;
            }
        });

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.switchCamera();
            }
        });

        btnTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnBGM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        presenter.setOnVideoRecordListener(new OnVideoRecordListener() {
            @Override
            public void onRecordStart(AudioEncodeConfig audioEncodeConfig, VideoEncodeConfig videoEncodeConfig, RecordConfig recordConfig) {
                Log.d(TAG, "onRecordStart: ");
            }

            @Override
            public void onRecording(long pts) {
                Log.d(TAG, "onRecording: " + pts);
            }

            @Override
            public void onRecordStop(RecordConfig recordConfig) {
                Log.d(TAG, "onRecordStop: " + recordConfig);
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
        presenter.cancelRecord();
    }

    @Override
    public RecordVideoPresenter createPresenter() {
        return new RecordVideoPresenter(this);
    }
}
