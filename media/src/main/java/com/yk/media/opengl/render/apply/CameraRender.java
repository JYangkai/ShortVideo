package com.yk.media.opengl.render.apply;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.yk.media.opengl.render.base.BaseRender;
import com.yk.media.opengl.render.core.OesCallbackRender;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer {
    private static final String TAG = "CameraRender";

    private final OesCallbackRender oesCallbackRender;
    private final BaseRender baseRender;

    private EGLContext eglContext;

    private int width;
    private int height;

    public CameraRender(Context context) {
        oesCallbackRender = new OesCallbackRender(context);
        baseRender = new BaseRender(context);

        oesCallbackRender.bindFbo(true);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        eglContext = ((EGL10) EGLContext.getEGL()).eglGetCurrentContext();
        oesCallbackRender.onCreate();
        baseRender.onCreate();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
        oesCallbackRender.onChange(width, height);
        baseRender.onChange(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        oesCallbackRender.onDrawSelf();
        int fboTextureId = oesCallbackRender.getFboTextureId();
        baseRender.onDraw(fboTextureId);
    }

    public int getFboTextureId() {
        return oesCallbackRender.getFboTextureId();
    }

    public void setOesSize(int width, int height) {
        oesCallbackRender.setOesSize(width, height);
    }

    public void setOnOesListener(OesCallbackRender.OnOesListener listener) {
        oesCallbackRender.setOnOesListener(listener);
    }

    public EGLContext getEglContext() {
        return eglContext;
    }
}
