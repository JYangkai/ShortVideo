package com.yk.shortvideo.ui.record;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.record.audio.AudioRecorder;
import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.RecordConfig;
import com.yk.media.record.listener.OnAudioRecordListener;
import com.yk.shortvideo.R;

import java.io.File;

public class MicRecordActivity extends AppCompatActivity {
    private static final String TAG = "MicRecordActivity";

    private AppCompatButton btnRecord;

    private final AudioRecorder audioRecorder = new AudioRecorder();

    private String path;

    private boolean isRecord = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mic_record);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        btnRecord = findViewById(R.id.btnRecord);
    }

    private void initData() {
        path = getExternalFilesDir("m4a").getPath() + File.separator + System.currentTimeMillis() + ".m4a";
    }

    private void bindEvent() {
        audioRecorder.setOnAudioRecordListener(new OnAudioRecordListener() {
            @Override
            public void onRecordStart(AudioEncodeConfig audioEncodeConfig, RecordConfig recordConfig) {
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

    @Override
    protected void onPause() {
        super.onPause();
        cancelRecord();
    }

    private void startRecord() {
        audioRecorder.prepare(path);
        audioRecorder.startRecord();
    }

    private void stopRecord() {
        audioRecorder.stopRecord();
    }

    private void cancelRecord() {
        audioRecorder.cancelRecord();
    }
}
