package com.yk.media.camera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Camera1 implements ICamera {

    private Camera camera;

    private CameraConfig cameraConfig = new CameraConfig();

    private boolean isFirstFrameCallback = false;

    private final List<OnCameraStateListener> onCameraStateListenerList = new ArrayList<>();
    private final List<OnCameraDataListener> onCameraDataListenerList = new ArrayList<>();

    @Override
    public void openCamera(Activity activity, Object surface) {
        closeCamera();

        int cameraNum = Camera.getNumberOfCameras();
        if (cameraNum <= 0) {
            onCameraError(new IllegalStateException("camera num <= 0"));
            return;
        }

        if (cameraNum >= 2) {
            camera = Camera.open(cameraConfig.getFacing());
        } else {
            camera = Camera.open();
        }

        if (camera == null) {
            onCameraError(new IllegalStateException("camera is null"));
            return;
        }

        try {
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
            if (cameraConfig.getCameraSize().getWidth() <= 0 || cameraConfig.getCameraSize().getHeight() <= 0) {
                Camera.Size size = CameraUtils.findTheBestSize(previewSizeList,
                        activity.getResources().getDisplayMetrics().widthPixels,
                        activity.getResources().getDisplayMetrics().heightPixels);
                cameraConfig.getCameraSize().setWidth(size.width);
                cameraConfig.getCameraSize().setHeight(size.height);
            }

            parameters.setPreviewSize(cameraConfig.getCameraSize().getWidth(), cameraConfig.getCameraSize().getHeight());
            parameters.setPreviewFormat(cameraConfig.getPreviewFormat());

            if (cameraConfig.getFacing() == Camera.CameraInfo.CAMERA_FACING_BACK) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }

            camera.setParameters(parameters);

            if (surface instanceof SurfaceHolder) {
                camera.setPreviewDisplay((SurfaceHolder) surface);
            } else if (surface instanceof SurfaceTexture) {
                camera.setPreviewTexture((SurfaceTexture) surface);
            }

            if (cameraConfig.getDisplayOrientation() < 0) {
                cameraConfig.setDisplayOrientation(CameraUtils.getDisplayOrientation(activity, cameraConfig.getFacing()));
            }
            camera.setDisplayOrientation(cameraConfig.getDisplayOrientation());

            isFirstFrameCallback = false;

            if (onCameraDataListenerList.isEmpty()) {
                camera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        if (!isFirstFrameCallback) {
                            onCameraOpen(cameraConfig);
                            isFirstFrameCallback = true;
                        }
                    }
                });
            } else {
                camera.addCallbackBuffer(new byte[cameraConfig.getCameraSize().getWidth() * cameraConfig.getCameraSize().getHeight() * 3 / 2]);
                camera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        camera.addCallbackBuffer(data);
                        if (!isFirstFrameCallback) {
                            onCameraOpen(cameraConfig);
                            isFirstFrameCallback = true;
                        }
                        onCameraData(data, cameraConfig);
                    }
                });
            }
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeCamera() {
        if (camera == null) {
            return;
        }
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
        onCameraClose();
    }

    @Override
    public void switchCamera() {
        if (cameraConfig.getFacing() == CameraConstants.Facing.BACK) {
            cameraConfig.setFacing(CameraConstants.Facing.FRONT);
        } else {
            cameraConfig.setFacing(CameraConstants.Facing.BACK);
        }
    }

    @Override
    public void switchCamera(int facing) {
        cameraConfig.setFacing(facing);
    }

    @Override
    public void setCameraConfig(CameraConfig cameraConfig) {
        this.cameraConfig = cameraConfig;
    }

    @Override
    public CameraConfig getCameraConfig() {
        return cameraConfig;
    }

    @Override
    public void addOnCameraStateListener(OnCameraStateListener onCameraStateListener) {
        onCameraStateListenerList.add(onCameraStateListener);
    }

    @Override
    public void addOnCameraDataListener(OnCameraDataListener onCameraDataListener) {
        onCameraDataListenerList.add(onCameraDataListener);
    }

    @Override
    public void setOnCameraStateListener(OnCameraStateListener onCameraStateListener) {
        onCameraStateListenerList.clear();
        onCameraStateListenerList.add(onCameraStateListener);
    }

    @Override
    public void setOnCameraDataListener(OnCameraDataListener onCameraDataListener) {
        onCameraDataListenerList.clear();
        onCameraDataListenerList.add(onCameraDataListener);
    }

    private void onCameraOpen(CameraConfig cameraConfig) {
        if (onCameraStateListenerList.isEmpty()) {
            return;
        }

        for (OnCameraStateListener onCameraStateListener : onCameraStateListenerList) {
            onCameraStateListener.onCameraOpen(cameraConfig);
        }
    }

    private void onCameraClose() {
        if (onCameraStateListenerList.isEmpty()) {
            return;
        }

        for (OnCameraStateListener onCameraStateListener : onCameraStateListenerList) {
            onCameraStateListener.onCameraClose();
        }
    }

    private void onCameraError(Exception e) {
        if (onCameraStateListenerList.isEmpty()) {
            return;
        }

        for (OnCameraStateListener onCameraStateListener : onCameraStateListenerList) {
            onCameraStateListener.onCameraError(e);
        }
    }

    private void onCameraData(byte[] data, CameraConfig cameraConfig) {
        if (onCameraDataListenerList.isEmpty()) {
            return;
        }

        for (OnCameraDataListener onCameraDataListener : onCameraDataListenerList) {
            onCameraDataListener.onCameraData(data, cameraConfig);
        }
    }
}
