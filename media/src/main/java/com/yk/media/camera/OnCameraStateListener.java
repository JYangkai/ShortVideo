package com.yk.media.camera;

public interface OnCameraStateListener {

    void onCameraOpen(CameraConfig cameraConfig);

    void onCameraClose();

    void onCameraError(Exception e);

}
