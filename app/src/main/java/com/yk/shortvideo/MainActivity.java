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

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton btnCameraRecord;

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
    }

    private void initData() {
        PermissionRequester.build(this)
                .permission(Manifest.permission.CAMERA)
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
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnCameraRecord) {
            startActivity(new Intent(this, CameraRecordActivity.class));
        }
    }
}