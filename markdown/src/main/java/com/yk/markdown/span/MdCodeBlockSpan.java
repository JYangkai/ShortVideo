package com.yk.markdown.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.TextPaint;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.style.bean.MdStyleCodeBlock;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdCodeBlockSpan extends MetricAffectingSpan implements LeadingMarginSpan, LineBackgroundSpan {
    private final int gapWidth;
    private final int backgroundColor;
    private final int textColor;
    private final int textSize;

    public MdCodeBlockSpan(BaseMdStyle style) {
        MdStyleCodeBlock codeBlock = style.getCodeBlock();
        gapWidth = codeBlock.getGapWidth();
        backgroundColor = codeBlock.getBackgroundColor();
        textColor = codeBlock.getTextColor();
        textSize = codeBlock.getTextSize();
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
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        int preColor = paint.getColor();

        paint.setColor(backgroundColor);

        canvas.drawRect(new Rect(left, top, right, bottom), paint);

        paint.setColor(preColor);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return gapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {

    }
}
