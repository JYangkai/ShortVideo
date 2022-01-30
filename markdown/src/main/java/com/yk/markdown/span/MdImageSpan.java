package com.yk.markdown.span;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;

import com.yk.markdown.utils.BitmapUtils;

public class MdImageSpan extends DynamicDrawableSpan {
    private final Context context;

    private final String path;

    public MdImageSpan(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    @Override
    public Drawable getDrawable() {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Activity activity = (Activity) context;
        int w = activity.getResources().getDisplayMetrics().widthPixels;
        int h = activity.getResources().getDisplayMetrics().heightPixels;
        Bitmap bitmap = BitmapUtils.getTheBestBitmap(path, w, h);
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        drawable.setBounds(0, 0,
                drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }
}
