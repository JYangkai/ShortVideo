package com.yk.media.opengl.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.yk.media.camera.Camera1;
import com.yk.media.camera.CameraConfig;
import com.yk.media.camera.CameraSize;
import com.yk.media.camera.ICamera;
import com.yk.media.camera.OnCameraStateListener;
import com.yk.media.opengl.render.apply.CameraRender;
import com.yk.media.opengl.render.core.OesCallbackRender;

import javax.microedition.khronos.egl.EGLContext;

public class CameraView extends GLSurfaceView implements OnCameraStateListener {

    private final Activity activity;
    private final ICamera camera = new Camera1();
    private final CameraRender render;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity) context;
        render = new CameraRender(context);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        camera.addOnCameraStateListener(this);
    }

    public void openCamera() {
        render.setOnOesListener(new OesCallbackRender.OnOesListener() {
            @Override
            public void onOes(SurfaceTexture surfaceTexture) {
                surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                    @Override
                    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                        requestRender();
                    }
                });
                camera.openCamera(activity, surfaceTexture);
            }
        });
    }

    public void closeCamera() {
        camera.closeCamera();
    }

    public void switchCamera() {
        camera.switchCamera();
        openCamera();
    }

    public CameraConfig getCameraConfig() {
        return camera.getCameraConfig();
    }

    public EGLContext getEglContext() {
        return render.getEglContext();
    }

    public int getFboTextureId() {
        return render.getFboTextureId();
    }

    @Override
    public void onCameraOpen(CameraConfig cameraConfig) {
        CameraSize cameraSize = cameraConfig.getCameraSize();
        render.setOesSize(cameraSize.getHeight(), cameraSize.getWidth());
        requestRender();
    }

    @Override
    public void onCameraClose() {

    }

    @Override
    public void onCameraError(Exception e) {
        closeCamera();
    }
}
