package com.yk.markdown.core.render.word;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.yk.markdown.bean.MdWord;
import com.yk.markdown.span.MdBoldItalicsSpan;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdBoldItalicsRender implements IMdWordRender {
    @Override
    public SpannableStringBuilder render(Context context, MdWord word, BaseMdStyle style) {
        String src = word.getSrc();
        src = src.substring(src.indexOf("***") + 3, src.lastIndexOf("***"));

        SpannableStringBuilder spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new MdBoldItalicsSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
