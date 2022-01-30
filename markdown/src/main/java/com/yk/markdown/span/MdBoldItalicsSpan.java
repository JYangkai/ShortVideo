package com.yk.markdown.span;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.style.bean.MdStyleBoldItalics;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdBoldItalicsSpan extends MetricAffectingSpan {
    private final int textColor;
    private final int textSize;

    public MdBoldItalicsSpan(BaseMdStyle style) {
        MdStyleBoldItalics boldItalics = style.getBoldItalics();
        textColor = boldItalics.getTextColor();
        textSize = boldItalics.getTextSize();
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint textPaint) {
        updateState(textPaint);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        updateState(tp);
    }

    private void updateState(TextPaint tp) {
        tp.setColor(textColor);
        tp.setTextSize(textSize);
        tp.setFakeBoldText(true);
        tp.setTextSkewX(-0.25f);
    }
}
