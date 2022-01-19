package com.yk.media.camera;

import android.graphics.ImageFormat;
import android.hardware.Camera;

public interface CameraConstants {

    interface Facing {
        int BACK = Camera.CameraInfo.CAMERA_FACING_BACK;
        int FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    interface Format {
        int NV21 = ImageFormat.NV21;
    }

}
