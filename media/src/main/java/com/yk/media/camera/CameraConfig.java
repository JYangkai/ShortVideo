package com.yk.media.camera;

public class CameraConfig {

    private int facing;
    private int previewFormat;
    private CameraSize cameraSize;
    private int displayOrientation;

    public CameraConfig() {
        facing = CameraConstants.Facing.BACK;
        previewFormat = CameraConstants.Format.NV21;
        cameraSize = new CameraSize(-1, -1);
        displayOrientation = -1;
    }

    public CameraConfig(int facing, int previewFormat, CameraSize cameraSize, int displayOrientation) {
        this.facing = facing;
        this.previewFormat = previewFormat;
        this.cameraSize = cameraSize;
        this.displayOrientation = displayOrientation;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public int getPreviewFormat() {
        return previewFormat;
    }

    public void setPreviewFormat(int previewFormat) {
        this.previewFormat = previewFormat;
    }

    public CameraSize getCameraSize() {
        return cameraSize;
    }

    public void setCameraSize(CameraSize cameraSize) {
        this.cameraSize = cameraSize;
    }

    public int getDisplayOrientation() {
        return displayOrientation;
    }

    public void setDisplayOrientation(int displayOrientation) {
        this.displayOrientation = displayOrientation;
    }

    @Override
    public String toString() {
        return "CameraConfig{" +
                "facing=" + facing +
                ", previewFormat=" + previewFormat +
                ", cameraSize=" + cameraSize +
                ", displayOrientation=" + displayOrientation +
                '}';
    }
}
