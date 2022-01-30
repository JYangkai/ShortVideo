package com.yk.markdown.span;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.style.bean.MdStyleItalics;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdItalicsSpan extends MetricAffectingSpan {
    private final int textColor;
    private final int textSize;

    public MdItalicsSpan(BaseMdStyle style) {
        MdStyleItalics italics = style.getItalics();
        textColor = italics.getTextColor();
        textSize = italics.getTextSize();
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
        tp.setTextSkewX(-0.25f);
    }
}
