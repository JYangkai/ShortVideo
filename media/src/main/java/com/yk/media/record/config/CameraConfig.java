package com.yk.media.record.config;

import com.yk.media.camera.CameraConstants;
import com.yk.media.camera.CameraSize;

public class CameraConfig {
    private int facing;
    private CameraSize cameraSize;

    public static CameraConfig getDefault() {
        return new CameraConfig(CameraConstants.Facing.BACK, new CameraSize(-1, -1));
    }

    public CameraConfig(int facing, CameraSize cameraSize) {
        this.facing = facing;
        this.cameraSize = cameraSize;
    }

    public int getFacing() {
        return facing;
    }

    public CameraConfig setFacing(int facing) {
        this.facing = facing;
        return this;
    }

    public CameraSize getCameraSize() {
        return cameraSize;
    }

    public CameraConfig setCameraSize(CameraSize cameraSize) {
        this.cameraSize = cameraSize;
        return this;
    }

    @Override
    public String toString() {
        return "CameraConfig{" +
                "facing=" + facing +
                ", cameraSize=" + cameraSize +
                '}';
    }
}
