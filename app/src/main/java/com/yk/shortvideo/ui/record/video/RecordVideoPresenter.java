package com.yk.shortvideo.ui.record.video;

import android.content.Context;

import com.yk.media.record.listener.OnVideoRecordListener;
import com.yk.media.record.video.VideoRecorder;
import com.yk.mvp.BaseMvpPresenter;
import com.yk.shortvideo.utils.FolderUtils;

import javax.microedition.khronos.egl.EGLContext;

public class RecordVideoPresenter extends BaseMvpPresenter<IRecordVideoView> {

    private final Context context;

    private final VideoRecorder videoRecorder = new VideoRecorder();

    public RecordVideoPresenter(Context context) {
        this.context = context;
    }

    public void startRecord(EGLContext eglContext, int fboTextureId, boolean enableAudio,
                            int width, int height, String bgmPath) {
        String path = FolderUtils.generateVideoPathForSuffix(context, ".mp4");

        videoRecorder.prepare(context, eglContext, fboTextureId, enableAudio, width, height, path, bgmPath);
        videoRecorder.startRecord();
    }

    public void stopRecord() {
        videoRecorder.stopRecord();
    }

    public void cancelRecord() {
        videoRecorder.cancelRecord();
    }

    public void setOnVideoRecordListener(OnVideoRecordListener onVideoRecordListener) {
        videoRecorder.setOnVideoRecordListener(onVideoRecordListener);
    }
}
