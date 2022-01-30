package com.yk.markdown.span;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.style.bean.MdStyleNormal;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdNormalSpan extends MetricAffectingSpan {
    private final int textColor;
    private final int textSize;

    public MdNormalSpan(BaseMdStyle style) {
        MdStyleNormal normal = style.getNormal();
        textColor = normal.getTextColor();
        textSize = normal.getTextSize();
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        updateState(tp);
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint tp) {
        updateState(tp);
    }

    private void updateState(TextPaint tp) {
        tp.setColor(textColor);
        tp.setTextSize(textSize);
    }
}
