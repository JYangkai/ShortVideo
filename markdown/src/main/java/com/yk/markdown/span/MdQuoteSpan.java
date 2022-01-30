package com.yk.markdown.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.text.style.LeadingMarginSpan;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.style.bean.MdStyleQuote;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdQuoteSpan extends MetricAffectingSpan implements LeadingMarginSpan {
    private final int stripeColor;
    private final int stripeWidth;
    private final int gapWidth;
    private final int textColor;
    private final int textSize;

    public MdQuoteSpan(BaseMdStyle style) {
        MdStyleQuote quote = style.getQuote();
        stripeColor = quote.getStripeColor();
        stripeWidth = quote.getStripeWidth();
        gapWidth = quote.getGapWidth();
        textColor = quote.getTextColor();
        textSize = quote.getTextSize();
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

    @Override
    public int getLeadingMargin(boolean first) {
        return stripeWidth + gapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        Paint.Style preStyle = p.getStyle();
        int preColor = p.getColor();

        p.setStyle(Paint.Style.FILL);
        p.setColor(stripeColor);

        c.drawRect(x, top, x + dir * stripeWidth, bottom, p);

        p.setStyle(preStyle);
        p.setColor(preColor);
    }
}
