package com.yk.markdown;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.yk.markdown.manager.RequestManager;
import com.yk.markdown.style.MdStyleManager;

public class Markdown {

    private static MdStyleManager.Style defaultStyle = MdStyleManager.Style.STANDARD;

    public static void configStyle(MdStyleManager.Style style) {
        defaultStyle = style;
    }

    public static void configStyle(String style) {
        switch (style) {
            case "Standard":
                configStyle(MdStyleManager.Style.STANDARD);
                break;
            case "Typora":
                configStyle(MdStyleManager.Style.TYPORA);
                break;
            case "Custom":
                configStyle(MdStyleManager.Style.CUSTOM);
                break;
            default:
                break;
        }
    }

    public static MdStyleManager.Style getDefaultStyle() {
        return defaultStyle;
    }

    private final RequestManager requestManager;

    private static volatile Markdown instance;

    private Markdown(Context context) {
        requestManager = new RequestManager(context);
    }

    public static Markdown getInstance(Context context) {
        if (instance == null) {
            synchronized (Markdown.class) {
                if (instance == null) {
                    instance = new Markdown(context);
                }
            }
        }
        return instance;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public static RequestManager with(Context context) {
        return Markdown.getInstance(context).getRequestManager();
    }

    public static RequestManager with(Fragment fragment) {
        return with(fragment.getActivity());
    }

    public static RequestManager with(android.app.Fragment fragment) {
        return with(fragment.getActivity());
    }

    public static RequestManager with(View view) {
        return with(view.getContext());
    }
}
