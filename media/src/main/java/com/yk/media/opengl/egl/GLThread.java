package com.yk.media.opengl.egl;

import android.opengl.GLSurfaceView;
import android.view.Surface;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLContext;

public class GLThread extends Thread {
    private EglHelper eglHelper;

    public boolean isCreate;
    public boolean isChange;

    private boolean isStart;
    private boolean isExit;

    private final Object object = new Object();

    private final ArrayList<Runnable> mEventQueue = new ArrayList<>();

    private Surface inputSurface;
    private EGLContext shareEGLContext;
    private int renderMode;
    private int renderRate;
    private int width;
    private int height;
    private GLSurfaceView.Renderer renderer;

    @Override
    public void run() {
        if (!checkState()) {
            return;
        }
        try {
            guardedRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkState() {
        return inputSurface != null;
    }

    private void guardedRun() throws InterruptedException {
        isExit = false;
        isStart = false;
        eglHelper = new EglHelper();
        eglHelper.init(shareEGLContext, inputSurface);
        Runnable event = null;
        while (true) {
            if (isExit) {
                release();
                break;
            }
            if (isStart) {
                if (renderMode == GLSurfaceView.RENDERMODE_WHEN_DIRTY) {
                    synchronized (object) {
                        object.wait();
                    }
                } else if (renderMode == GLSurfaceView.RENDERMODE_CONTINUOUSLY) {
                    sleep(1000 / renderRate);
                } else {
                    throw new IllegalArgumentException("render mode error");
                }
            }

            if (!mEventQueue.isEmpty()) {
                event = mEventQueue.remove(0);
            }

            if (event != null) {
                event.run();
                event = null;
                continue;
            }

            onCreate();
            onChange(width, height);
            onDrawFrame();
            isStart = true;
        }
    }

    private void onCreate() {
        if (!isCreate) {
            return;
        }
        isCreate = false;
        renderer.onSurfaceCreated(null, null);
    }

    private void onChange(int width, int height) {
        if (!isChange) {
            return;
        }
        isChange = false;
        renderer.onSurfaceChanged(null, width, height);
    }

    private void onDrawFrame() {
        renderer.onDrawFrame(null);
        if (!isStart) {
            renderer.onDrawFrame(null);
        }
        eglHelper.swapBuffers();
    }

    public void requestRender() {
        synchronized (object) {
            object.notifyAll();
        }
    }

    public void onDestroy() {
        isExit = true;
        requestRender();
    }

    private void release() {
        if (eglHelper != null) {
            eglHelper.destroyEgl();
            eglHelper = null;
        }
    }

    public EGLContext getEGLContext() {
        if (eglHelper != null) {
            return eglHelper.getEglContext();
        }
        return null;
    }

    public void queueEvent(Runnable r) {
        if (r == null) {
            return;
        }
        synchronized (object) {
            mEventQueue.add(r);
            object.notifyAll();
        }
    }

    public Surface getInputSurface() {
        return inputSurface;
    }

    public GLThread setInputSurface(Surface inputSurface) {
        this.inputSurface = inputSurface;
        return this;
    }

    public EGLContext getShareEGLContext() {
        return shareEGLContext;
    }

    public GLThread setShareEGLContext(EGLContext shareEGLContext) {
        this.shareEGLContext = shareEGLContext;
        return this;
    }

    public int getRenderMode() {
        return renderMode;
    }

    public GLThread setRenderMode(int renderMode) {
        this.renderMode = renderMode;
        return this;
    }

    public int getRenderRate() {
        return renderRate;
    }

    public GLThread setRenderRate(int renderRate) {
        this.renderRate = renderRate;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public GLThread setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public GLThread setHeight(int height) {
        this.height = height;
        return this;
    }

    public GLSurfaceView.Renderer getRenderer() {
        return renderer;
    }

    public GLThread setRenderer(GLSurfaceView.Renderer renderer) {
        this.renderer = renderer;
        return this;
    }
}
