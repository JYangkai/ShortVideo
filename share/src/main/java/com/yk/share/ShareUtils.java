package com.yk.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class ShareUtils {
    private static final String TYPE_TEXT = "text/plain";
    private static final String TYPE_IMAGE = "image/*";
    private static final String TYPE_VIDEO = "video/*";

    private static final String TYPE_JPG = "image/jpg";
    private static final String TYPE_PNG = "image/png";
    private static final String TYPE_GIF = "image/gif";

    private static final String TYPE_MP4 = "video/mp4";
    private static final String TYPE_3GP = "video/3gp";

    public static boolean shareText(Context context, String content) {
        return share(context, Intent.EXTRA_TEXT, content, TYPE_TEXT);
    }

    public static boolean shareFile(Context context, Uri uri) {
        return share(context, Intent.EXTRA_STREAM, uri, "*/*");
    }

    public static boolean shareMp4(Context context, Uri uri) {
        return shareMedia(context, uri, TYPE_MP4);
    }

    public static boolean share3gp(Context context, Uri uri) {
        return shareMedia(context, uri, TYPE_3GP);
    }

    public static boolean shareJpg(Context context, Uri uri) {
        return shareMedia(context, uri, TYPE_JPG);
    }

    public static boolean sharePng(Context context, Uri uri) {
        return shareMedia(context, uri, TYPE_PNG);
    }

    public static boolean shareGif(Context context, Uri uri) {
        return shareMedia(context, uri, TYPE_GIF);
    }

    public static boolean shareMedia(Context context, Uri uri, String type) {
        return share(context, Intent.EXTRA_STREAM, uri, type);
    }

    public static boolean share(Context context, String extraName, String content, String type) {
        if (context == null) {
            return false;
        }

        if (TextUtils.isEmpty(extraName)) {
            return false;
        }

        if (TextUtils.isEmpty(content)) {
            return false;
        }

        if (TextUtils.isEmpty(type)) {
            return false;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(extraName, content);
        intent.setType(type);

        Intent share = Intent.createChooser(intent, null);
        context.startActivity(share);

        return true;
    }

    public static boolean share(Context context, String extraName, Uri uri, String type) {
        if (context == null) {
            return false;
        }

        if (TextUtils.isEmpty(extraName)) {
            return false;
        }

        if (uri == null) {
            return false;
        }

        if (TextUtils.isEmpty(type)) {
            return false;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(extraName, uri);
        intent.setType(type);

        Intent share = Intent.createChooser(intent, null);
        context.startActivity(share);

        return true;
    }

}
