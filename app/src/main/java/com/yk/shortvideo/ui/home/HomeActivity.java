package com.yk.shortvideo.ui.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.yk.mvp.BaseMvpActivity;
import com.yk.permissionrequester.PermissionFragment;
import com.yk.permissionrequester.PermissionRequester;
import com.yk.shortvideo.R;
import com.yk.shortvideo.ui.record.video.RecordVideoActivity;

import java.util.List;

public class HomeActivity extends BaseMvpActivity<IHomeView, HomePresenter> implements IHomeView {

    private Toolbar toolbar;
    private AppCompatButton btnRecordVideo;
    private AppCompatButton btnRecordAudio;
    private AppCompatButton btnEditVideo;
    private AppCompatButton btnMakeAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        btnRecordVideo = findViewById(R.id.btnRecordVideo);
        btnRecordAudio = findViewById(R.id.btnRecordAudio);
        btnEditVideo = findViewById(R.id.btnEditVideo);
        btnMakeAlbum = findViewById(R.id.btnMakeAlbum);
    }

    private void initData() {
        initToolbar();
        checkPermission();
    }

    private void bindEvent() {
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RecordVideoActivity.class));
            }
        });

        btnRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEditVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnMakeAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initToolbar() {
        toolbar.setTitle("主页");
        setSupportActionBar(toolbar);
    }

    private void checkPermission() {
        PermissionRequester.build(this)
                .permission(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .request(new PermissionFragment.OnPermissionRequestListener() {
                    @Override
                    public void onRequestSuccess(boolean success) {
                        if (!success) {
                            finish();
                        }
                    }

                    @Override
                    public void onGrantedList(List<String> grantedList) {

                    }

                    @Override
                    public void onDeniedList(List<String> deniedList) {

                    }
                });
    }

    @Override
    public HomePresenter createPresenter() {
        return new HomePresenter();
    }
}