package com.yk.markdown.core.render.section;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.span.MdQuoteSpan;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdQuoteRender implements IMdSectionRender {
    @Override
    public SpannableStringBuilder render(Context context, MdSection section, BaseMdStyle style) {
        String src = section.getSrc();
        src = src.substring(src.indexOf(" ") + 1);

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableStringBuilder spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new MdQuoteSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
