package com.yk.media.record.video;

import android.content.Context;

import com.yk.media.camera.CameraSize;
import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.CameraConfig;
import com.yk.media.record.config.MicConfig;
import com.yk.media.record.config.RecordConfig;
import com.yk.media.record.config.VideoEncodeConfig;

import javax.microedition.khronos.egl.EGLContext;

public interface IVideoRecord {

    void prepare(Context context, EGLContext eglContext, int fboTextureId,
                 boolean enableAudio, int facing, CameraSize cameraSize, String path);

    void prepare(Context context, EGLContext eglContext, int fboTextureId,
                 MicConfig micConfig, CameraConfig cameraConfig,
                 AudioEncodeConfig audioEncodeConfig, VideoEncodeConfig videoEncodeConfig,
                 RecordConfig recordConfig);

    void startRecord();

    void stopRecord();

    void cancelRecord();

}
