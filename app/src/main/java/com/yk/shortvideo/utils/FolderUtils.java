package com.yk.shortvideo.utils;

import android.content.Context;

import java.io.File;

public class FolderUtils {
    interface Folder {
        String AUDIO = "音频";
        String VIDEO = "视频";
        String IMAGE = "图片";
    }

    public static File getFolder(Context context, String name) {
        return context.getExternalFilesDir(name);
    }

    public static File getAudioFolder(Context context) {
        return getFolder(context, Folder.AUDIO);
    }

    public static File getVideoFolder(Context context) {
        return getFolder(context, Folder.VIDEO);
    }

    public static File getImageFolder(Context context) {
        return getFolder(context, Folder.IMAGE);
    }

    public static String generateAudioPath(Context context, String suffix) {
        File folder = getAudioFolder(context);
        if (folder == null) {
            return null;
        }
        return folder.getPath() + File.separator + System.currentTimeMillis() + suffix;
    }

    public static String generateVideoPath(Context context, String suffix) {
        File folder = getVideoFolder(context);
        if (folder == null) {
            return null;
        }
        return folder.getPath() + File.separator + System.currentTimeMillis() + suffix;
    }

    public static String generateImagePath(Context context, String suffix) {
        File folder = getImageFolder(context);
        if (folder == null) {
            return null;
        }
        return folder.getPath() + File.separator + System.currentTimeMillis() + suffix;
    }

}
