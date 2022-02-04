package com.yk.shortvideo.ui.source;

import android.content.Context;
import android.util.Log;

import com.yk.media.source.Audio;
import com.yk.media.source.MediaSourceUtils;
import com.yk.media.transcode.audio.AudioTranscoder;
import com.yk.media.transcode.bean.DecodeAudioResult;
import com.yk.media.transcode.bean.EncodeResult;
import com.yk.media.transcode.listener.OnAudioTranscodeListener;
import com.yk.mvp.BaseMvpPresenter;
import com.yk.rxsimpler.Observable;
import com.yk.rxsimpler.Subscriber;
import com.yk.shortvideo.data.bean.AudioSource;
import com.yk.shortvideo.utils.FolderUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioSourcePresenter extends BaseMvpPresenter<IAudioSourceView> {
    private static final String TAG = "AudioSourcePresenter";

    private final Context context;

    private final AudioTranscoder audioTranscoder = new AudioTranscoder();

    public AudioSourcePresenter(Context context) {
        this.context = context;
    }

    public void loadAudioSource() {
        Observable.fromCallable(new Observable.OnCallable<List<AudioSource>>() {
            @Override
            public List<AudioSource> call() {
                List<Audio> audioList = MediaSourceUtils.getLocalAudio(context);
                return audioToAudioSource(audioList);
            }
        })
                .map(new Observable.Function1<List<AudioSource>, List<AudioSource>>() {
                    @Override
                    public List<AudioSource> call(List<AudioSource> audioSources) {
                        File audioFolder = FolderUtils.getAudioFolder(context);
                        if (audioFolder == null) {
                            return audioSources;
                        }

                        File[] audioFiles = audioFolder.listFiles();
                        if (audioFiles == null || audioFiles.length == 0) {
                            return audioSources;
                        }

                        List<AudioSource> list = new ArrayList<>();

                        for (File audioFile : audioFiles) {
                            Audio audio = new Audio(audioFile.getName(), audioFile.getPath(), -1);
                            AudioSource audioSource = new AudioSource(audio);
                            audioSource.setState(AudioSource.State.CAN_USE);
                            list.add(audioSource);
                        }

                        for (AudioSource audioSource : audioSources) {
                            if (list.contains(audioSource)) {
                                continue;
                            }
                            list.add(audioSource);
                        }

                        return list;
                    }
                })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<List<AudioSource>>() {
                    @Override
                    public void onNext(List<AudioSource> audioSources) {
                        if (getView() != null) {
                            getView().onLoadAudioSource(audioSources);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Exception e) {
                        if (getView() != null) {
                            getView().onLoadAudioSourceError(e);
                        }
                    }
                });
    }

    public void transcode(AudioSource audioSource) {
        String inputPath = audioSource.getAudio().getPath();
        String outputPath = FolderUtils.generateAudioPathForName(context, FolderUtils.getNameForSuffix(inputPath, ".aac"));

        audioTranscoder.setOnAudioTranscodeListener(new OnAudioTranscodeListener() {
            @Override
            public void onAudioTranscodeStart() {
                Log.d(TAG, "onAudioTranscodeStart: ");
            }

            @Override
            public void onAudioTranscoding(long progress) {
                Log.d(TAG, "onAudioTranscoding: " + progress);
            }

            @Override
            public void onAudioTranscodeStop(DecodeAudioResult decodeAudioResult, EncodeResult encodeResult) {
                Log.d(TAG, "onAudioTranscodeStop: ");
                if (getView() != null) {
                    getView().onTranscodeComplete(audioSource, encodeResult.getAacPath());
                }
            }

            @Override
            public void onAudioTranscodeCancel() {
                Log.d(TAG, "onAudioTranscodeCancel: ");
            }

            @Override
            public void onAudioTranscodeError(Exception e) {
                Log.e(TAG, "onAudioTranscodeError: ", e);
                if (getView() != null) {
                    getView().onTranscodeError(e);
                }
            }
        });
        audioTranscoder.start(inputPath, outputPath);
    }

    private List<AudioSource> audioToAudioSource(List<Audio> audioList) {
        List<AudioSource> audioSourceList = new ArrayList<>();

        if (audioList == null || audioList.isEmpty()) {
            return audioSourceList;
        }

        for (Audio audio : audioList) {
            AudioSource audioSource = new AudioSource(audio);
            String name = audio.getName();
            String suffix = name.substring(name.lastIndexOf("."));
            if (suffix.equals(".aac")) {
                audioSource.setState(AudioSource.State.CAN_USE);
            } else {
                audioSource.setState(AudioSource.State.NEED_TRANSCODE);
            }
            audioSourceList.add(audioSource);
        }

        return audioSourceList;
    }
}
