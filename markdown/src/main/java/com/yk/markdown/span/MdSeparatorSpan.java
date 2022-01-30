package com.yk.markdown.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.style.bean.MdStyleSeparator;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdSeparatorSpan implements LineBackgroundSpan {
    private final int color;
    private final int size;

    public MdSeparatorSpan(BaseMdStyle style) {
        MdStyleSeparator separator = style.getSeparator();
        color = separator.getColor();
        size = separator.getSize();
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        int preColor = paint.getColor();
        float preStrokeWidth = paint.getStrokeWidth();

        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawLine(left, (top + bottom) / 2.0f, right, (top + bottom) / 2.0f, paint);

        paint.setColor(preColor);
        paint.setStrokeWidth(preStrokeWidth);
    }
}
