package com.yk.shortvideo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.permissionrequester.PermissionFragment;
import com.yk.permissionrequester.PermissionRequester;
import com.yk.shortvideo.ui.record.CameraRecordActivity;
import com.yk.shortvideo.ui.record.MicRecordActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton btnCameraRecord;
    private AppCompatButton btnMicRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        btnCameraRecord = findViewById(R.id.btnCameraRecord);
        btnMicRecord = findViewById(R.id.btnMicRecord);
    }

    private void initData() {
        PermissionRequester.build(this)
                .permission(Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request(new PermissionFragment.OnPermissionRequestListener() {
                    @Override
                    public void onRequestSuccess(boolean b) {
                        if (!b) {
                            finish();
                        }
                    }

                    @Override
                    public void onGrantedList(List<String> list) {

                    }

                    @Override
                    public void onDeniedList(List<String> list) {

                    }
                });
    }

    private void bindEvent() {
        btnCameraRecord.setOnClickListener(this);
        btnMicRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnCameraRecord) {
            startActivity(new Intent(this, CameraRecordActivity.class));
        } else if (id == R.id.btnMicRecord) {
            startActivity(new Intent(this, MicRecordActivity.class));
        }
    }
}