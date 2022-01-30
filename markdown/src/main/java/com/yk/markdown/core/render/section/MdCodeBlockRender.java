package com.yk.markdown.core.render.section;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.span.MdCodeBlockSpan;
import com.yk.markdown.style.style.BaseMdStyle;

public class MdCodeBlockRender implements IMdSectionRender {
    @Override
    public SpannableStringBuilder render(Context context, MdSection section, BaseMdStyle style) {
        String src = section.getSrc();
        src = "\n" + src.substring(src.indexOf("```") + 4, src.lastIndexOf("```")) + "\n";

        if (TextUtils.isEmpty(src)) {
            src = " ";
        }

        SpannableStringBuilder spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new MdCodeBlockSpan(style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }
}
