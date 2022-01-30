package com.yk.markdown.manager;

import android.content.Context;
import android.widget.TextView;

import com.yk.markdown.Markdown;
import com.yk.markdown.style.MdStyleManager;

public class RequestManager {
    private final Context context;

    private String src;
    private MdStyleManager.Style style;

    public RequestManager(Context context) {
        this.context = context;
    }

    public RequestManager load(String src) {
        this.src = src;
        return this;
    }

    public RequestManager style(MdStyleManager.Style style) {
        this.style = style;
        return this;
    }

    public void into(TextView tv) {
        if (style == null) {
            style = Markdown.getDefaultStyle();
        }
        RequestThreadManager.getInstance().executeIo(context, src, MdStyleManager.getStyle(context, style), tv);
        style = null;
    }

}
