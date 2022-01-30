package com.yk.markdown.core.render;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;
import com.yk.markdown.core.render.section.IMdSectionRender;
import com.yk.markdown.core.render.section.MdCodeBlockRender;
import com.yk.markdown.core.render.section.MdOrderedListRender;
import com.yk.markdown.core.render.section.MdQuoteRender;
import com.yk.markdown.core.render.section.MdSeparatorRender;
import com.yk.markdown.core.render.section.MdTitleRender;
import com.yk.markdown.core.render.section.MdUnorderedListRender;
import com.yk.markdown.style.style.BaseMdStyle;

/**
 * 渲染section
 */
public class MdSectionRender {
    private final IMdSectionRender quoteRender = new MdQuoteRender();
    private final IMdSectionRender codeBlockRender = new MdCodeBlockRender();
    private final IMdSectionRender orderedListRender = new MdOrderedListRender();
    private final IMdSectionRender unorderedListRender = new MdUnorderedListRender();
    private final IMdSectionRender titleRender = new MdTitleRender();
    private final IMdSectionRender separatorRender = new MdSeparatorRender();

    /**
     * 渲染section
     */
    public SpannableStringBuilder render(Context context, MdSection section, BaseMdStyle style) {
        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        MdType type = section.getType();

        switch (type) {
            case QUOTE:
                spanStrBuilder.append(quoteRender.render(context, section, style));
                break;
            case CODE_BLOCK:
                spanStrBuilder.append(codeBlockRender.render(context, section, style));
                break;
            case ORDERED_LIST:
                spanStrBuilder.append(orderedListRender.render(context, section, style));
                break;
            case UNORDERED_LIST:
                spanStrBuilder.append(unorderedListRender.render(context, section, style));
                break;
            case TITLE:
                spanStrBuilder.append(titleRender.render(context, section, style));
                break;
            case SEPARATOR:
                spanStrBuilder.append(separatorRender.render(context, section, style));
                break;
            default:
                spanStrBuilder.append(section.getSrc());
                break;
        }

        return spanStrBuilder;
    }
}
