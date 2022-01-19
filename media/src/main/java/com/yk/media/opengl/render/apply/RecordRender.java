package com.yk.media.opengl.render.apply;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.yk.media.opengl.render.base.BaseRender;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RecordRender implements GLSurfaceView.Renderer {

    private int textureId;

    private final BaseRender baseRender;

    public RecordRender(Context context) {
        baseRender = new BaseRender(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        baseRender.onCreate();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        baseRender.onChange(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        baseRender.onDraw(textureId);
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }
}
