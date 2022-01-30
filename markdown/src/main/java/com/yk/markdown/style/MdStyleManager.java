package com.yk.markdown.style;

import android.content.Context;

import com.yk.markdown.style.style.BaseMdStyle;
import com.yk.markdown.style.style.CustomMdStyle;
import com.yk.markdown.style.style.StandardMdStyle;
import com.yk.markdown.style.style.TyporaMdStyle;

public class MdStyleManager {
    public enum Style {
        STANDARD,
        TYPORA,
        CUSTOM,
    }

    public static BaseMdStyle getStyle(Context context, Style style) {
        if (style == null) {
            throw new RuntimeException("style is null");
        }
        BaseMdStyle baseMdStyle = null;
        switch (style) {
            case STANDARD:
                baseMdStyle = new StandardMdStyle();
                break;
            case TYPORA:
                baseMdStyle = new TyporaMdStyle();
                break;
            case CUSTOM:
                baseMdStyle = new CustomMdStyle(context);
                break;
        }

        baseMdStyle.init();

        return baseMdStyle;
    }
}
