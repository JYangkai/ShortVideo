package com.yk.markdown.core.render.word;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.yk.markdown.bean.MdWord;
import com.yk.markdown.span.MdNormalSpan;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdNormalRender implements IMdWordRender {
    @Override
    public SpannableStringBuilder render(Context context, MdWord word, BaseMdStyle style) {
        String src = word.getSrc();

        SpannableStringBuilder spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new MdNormalSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
