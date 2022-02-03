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

    public static String generateAudioPathForSuffix(Context context, String suffix) {
        File folder = getAudioFolder(context);
        if (folder == null) {
            return null;
        }
        return folder.getPath() + File.separator + System.currentTimeMillis() + suffix;
    }

    public static String generateVideoPathForSuffix(Context context, String suffix) {
        File folder = getVideoFolder(context);
        if (folder == null) {
            return null;
        }
        return folder.getPath() + File.separator + System.currentTimeMillis() + suffix;
    }

    public static String generateImagePathForSuffix(Context context, String suffix) {
        File folder = getImageFolder(context);
        if (folder == null) {
            return null;
        }
        return folder.getPath() + File.separator + System.currentTimeMillis() + suffix;
    }

    public static String generateAudioPathForName(Context context, String name) {
        File folder = getAudioFolder(context);
        if (folder == null) {
            return null;
        }
        return folder.getPath() + File.separator + name;
    }

    public static String generateVideoPathForName(Context context, String name) {
        File folder = getVideoFolder(context);
        if (folder == null) {
            return null;
        }
        return folder.getPath() + File.separator + name;
    }

    public static String generateImagePathForName(Context context, String name) {
        File folder = getImageFolder(context);
        if (folder == null) {
            return null;
        }
        return folder.getPath() + File.separator + name;
    }

    public static String getAACName(String inputPath) {
        int start = inputPath.lastIndexOf("/") + 1;
        int end = inputPath.lastIndexOf(".");
        return inputPath.substring(start, end) + ".aac";
    }

}
