package com.yk.media.camera;

import android.app.Activity;

public interface ICamera {

    void openCamera(Activity activity, Object surface);

    void closeCamera();

    void switchCamera();

    void switchCamera(int facing);

    void setCameraConfig(CameraConfig cameraConfig);

    CameraConfig getCameraConfig();

    void addOnCameraStateListener(OnCameraStateListener onCameraStateListener);

    void addOnCameraDataListener(OnCameraDataListener onCameraDataListener);

    void setOnCameraStateListener(OnCameraStateListener onCameraStateListener);

    void setOnCameraDataListener(OnCameraDataListener onCameraDataListener);

}
