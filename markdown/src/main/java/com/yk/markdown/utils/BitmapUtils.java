package com.yk.markdown.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

public class BitmapUtils {

    public static Bitmap getTheBestBitmap(String path, int screenW, int screenH) {
        // 先获取正确旋转后的bitmap
        Bitmap rotateBitmap = getRotateBitmap(path);
        // 再获取尺寸适合的bitmap
        return getScaleBitmap(rotateBitmap, screenW, screenH);
    }

    private static Bitmap getScaleBitmap(Bitmap origin, int screenW, int screenH) {
        if (origin == null) {
            return null;
        }
        float scale = getBitmapScale(origin.getWidth(), origin.getHeight(), screenW, screenH);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(origin, 0, 0, origin.getWidth(), origin.getHeight(), matrix, true);
    }

    private static Bitmap getRotateBitmap(String path) {
        Bitmap origin = BitmapFactory.decodeFile(path);
        if (origin == null) {
            return null;
        }
        int degree = getBitmapOrientation(getExifInterfaceFromPath(path));
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(origin, 0, 0, origin.getWidth(), origin.getHeight(), matrix, true);
    }

    private static float getBitmapScale(int bitmapW, int bitmapH, int screenW, int screenH) {
        return (float) screenW / bitmapW;
    }

    private static ExifInterface getExifInterfaceFromPath(String path) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exifInterface;
    }

    private static int getBitmapOrientation(ExifInterface exifInterface) {
        int degree = 0;
        if (exifInterface == null) {
            return degree;
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            default:
                degree = 0;
        }
        return degree;
    }

}
