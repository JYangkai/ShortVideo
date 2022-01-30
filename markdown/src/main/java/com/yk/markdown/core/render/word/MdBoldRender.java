package com.yk.markdown.core.render.word;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.yk.markdown.bean.MdWord;
import com.yk.markdown.span.MdBoldSpan;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdBoldRender implements IMdWordRender {
    @Override
    public SpannableStringBuilder render(Context context, MdWord word, BaseMdStyle style) {
        String src = word.getSrc();
        src = src.substring(src.indexOf("**") + 2, src.lastIndexOf("**"));

        SpannableStringBuilder spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new MdBoldSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
