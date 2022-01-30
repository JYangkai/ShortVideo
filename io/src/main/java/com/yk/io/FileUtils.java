package com.yk.io;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File file = new File(path);

        return file.exists();
    }

    public static boolean outputTextToFile(String content, String outputPath) {
        return outputTextToFile(content, outputPath, true);
    }

    public static boolean outputTextToFile(String content, String outputPath, boolean replace) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        return output(content.getBytes(), outputPath, replace);
    }

    public static boolean output(byte[] data, String outputPath, boolean replace) {
        if (data == null) {
            return false;
        }

        if (TextUtils.isEmpty(outputPath)) {
            return false;
        }

        File file = new File(outputPath);
        if (file.exists() && replace) {
            file.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String inputText(String inputPath) {
        return new String(input(inputPath));
    }

    public static byte[] input(String inputPath) {
        if (TextUtils.isEmpty(inputPath)) {
            return null;
        }

        File file = new File(inputPath);
        if (!file.exists()) {
            return null;
        }

        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            data = new byte[fis.available()];
            fis.read(data);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
