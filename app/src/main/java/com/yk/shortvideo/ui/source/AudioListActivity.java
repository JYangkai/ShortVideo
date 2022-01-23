package com.yk.shortvideo.ui.source;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.media.source.Audio;
import com.yk.media.source.MediaSourceUtils;
import com.yk.shortvideo.R;
import com.yk.shortvideo.data.adapter.AudioAdapter;

import java.util.ArrayList;
import java.util.List;

public class AudioListActivity extends AppCompatActivity {
    public static final String EXTRA_BACK_AUDIO = "extra_back_audio";

    private RecyclerView rvAudio;

    private final List<Audio> audioList = new ArrayList<>();
    private AudioAdapter audioAdapter;

    public static void startForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, AudioListActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        rvAudio = findViewById(R.id.rvAudio);
    }

    private void initData() {
        initRvAudio();
        loadAudio();
    }

    private void bindEvent() {
        audioAdapter.setOnAudioItemClickListener(new AudioAdapter.OnAudioItemClickListener() {
            @Override
            public void onAudioItemClick(Audio audio) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_BACK_AUDIO, audio);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initRvAudio() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        audioAdapter = new AudioAdapter(audioList);
        rvAudio.setLayoutManager(layoutManager);
        rvAudio.setAdapter(audioAdapter);
    }

    private void loadAudio() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Audio> list = MediaSourceUtils.getLocalAudio(AudioListActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        audioList.clear();
                        audioList.addAll(list);
                        audioAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
