package com.yk.markdown.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.LineBackgroundSpan;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

import com.yk.markdown.style.bean.MdStyleSeparator;
import com.yk.markdown.style.bean.MdStyleTitle;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdTitleSpan extends MetricAffectingSpan implements LineBackgroundSpan {
    private final int level;
    private final int textColor;
    private final int textSize;

    private BaseMdStyle style;

    public MdTitleSpan(BaseMdStyle style) {
        this(1, style);
    }

    public MdTitleSpan(int level, BaseMdStyle style) {
        this.style = style;

        this.level = level;

        MdStyleTitle title = style.getTitle();

        textColor = title.getTextColor();
        switch (level) {
            case 1:
                textSize = title.getTextSize1();
                break;
            case 2:
                textSize = title.getTextSize2();
                break;
            case 3:
                textSize = title.getTextSize3();
                break;
            case 4:
                textSize = title.getTextSize4();
                break;
            case 5:
            default:
                textSize = title.getTextSize5();
                break;
        }
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
        tp.setFakeBoldText(true);
        tp.setColor(textColor);
        tp.setTextSize(textSize);
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        if (level > 2) {
            return;
        }

        int preColor = paint.getColor();
        float preStrokeWidth = paint.getStrokeWidth();

        MdStyleSeparator separator = style.getSeparator();

        paint.setColor(separator.getColor());
        paint.setStrokeWidth(separator.getSize());
        canvas.drawLine(left, bottom, right, bottom, paint);

        paint.setColor(preColor);
        paint.setStrokeWidth(preStrokeWidth);
    }
}
