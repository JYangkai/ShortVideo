package com.yk.shortvideo.ui.source;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.eventposter.EventPoster;
import com.yk.media.play.audio.AudioPlayer;
import com.yk.mvp.BaseMvpActivity;
import com.yk.shortvideo.R;
import com.yk.shortvideo.data.adapter.AudioSourceAdapter;
import com.yk.shortvideo.data.bean.AudioSource;
import com.yk.shortvideo.data.event.UseBGMEvent;

import java.util.ArrayList;
import java.util.List;

public class AudioSourceActivity extends BaseMvpActivity<IAudioSourceView, AudioSourcePresenter> implements IAudioSourceView {
    private static final String TAG = "AudioSourceActivity";

    private Toolbar toolbar;
    private RecyclerView rvAudioSource;

    private final List<AudioSource> audioSourceList = new ArrayList<>();
    private AudioSourceAdapter audioSourceAdapter;

    private final AudioPlayer audioPlayer = new AudioPlayer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_source);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        rvAudioSource = findViewById(R.id.rvAudioSource);
    }

    private void initData() {
        initToolbar();
        initAudioSourceAdapter();
        presenter.loadAudioSource();
    }

    private void bindEvent() {
        audioSourceAdapter.setOnUseClickListener(new AudioSourceAdapter.OnUseClickListener() {
            @Override
            public void onUseClick(AudioSource source) {
                switch (source.getState()) {
                    case CAN_USE:
                        useBGM(source);
                        break;
                    case NEED_TRANSCODE:
                        transcode(source);
                        break;
                }
            }

            @Override
            public void onPlayClick(AudioSource source) {
                Log.d(TAG, "onPlayClick: " + source);
                audioPlayer.play(source.getAudio().getPath());
            }
        });
    }

    private void initToolbar() {
        toolbar.setTitle("音频库");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void initAudioSourceAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        audioSourceAdapter = new AudioSourceAdapter(audioSourceList);
        rvAudioSource.setLayoutManager(layoutManager);
        rvAudioSource.setAdapter(audioSourceAdapter);
    }

    private void useBGM(AudioSource audioSource) {
        EventPoster.getInstance().post(new UseBGMEvent(audioSource));
        finish();
    }

    private void transcode(AudioSource audioSource) {
        presenter.transcode(audioSource);
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioPlayer.stop();
    }

    @Override
    public AudioSourcePresenter createPresenter() {
        return new AudioSourcePresenter(this);
    }

    @Override
    public void onLoadAudioSource(List<AudioSource> list) {
        audioSourceList.clear();
        audioSourceList.addAll(list);
        audioSourceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadAudioSourceError(Exception e) {

    }

    @Override
    public void onTranscodeComplete(AudioSource audioSource, String outputPath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int index = audioSourceList.indexOf(audioSource);
                audioSourceList.get(index).setState(AudioSource.State.CAN_USE);
                audioSourceList.get(index).getAudio().setPath(outputPath);
                audioSourceAdapter.notifyItemChanged(index);
            }
        });
    }

    @Override
    public void onTranscodeError(Exception e) {

    }
}
