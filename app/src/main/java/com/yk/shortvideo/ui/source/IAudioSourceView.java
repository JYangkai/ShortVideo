package com.yk.shortvideo.ui.source;

import com.yk.mvp.BaseMvpView;
import com.yk.shortvideo.data.bean.AudioSource;

import java.util.List;

public interface IAudioSourceView extends BaseMvpView {

    void onLoadAudioSource(List<AudioSource> list);

    void onLoadAudioSourceError(Exception e);

    void onTranscodeComplete(AudioSource audioSource, String outputPath);

    void onTranscodeError(Exception e);

}
