package com.yk.media.record.video;

import android.content.Context;

import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.RecordConfig;
import com.yk.media.record.config.VideoEncodeConfig;

import javax.microedition.khronos.egl.EGLContext;

public interface IVideoRecord {

    void prepare(Context context, EGLContext eglContext, int fboTextureId, boolean enableAudio,
                 int width, int height, String path);

    void prepare(Context context, EGLContext eglContext, int fboTextureId,
                 AudioEncodeConfig audioEncodeConfig,
                 VideoEncodeConfig videoEncodeConfig,
                 RecordConfig recordConfig);

    void startRecord();

    void stopRecord();

    void cancelRecord();

}
