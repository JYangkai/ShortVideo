package com.yk.markdown.core;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;
import com.yk.markdown.bean.MdWord;
import com.yk.markdown.core.render.MdSectionRender;
import com.yk.markdown.core.render.MdWordRender;
import com.yk.markdown.style.style.BaseMdStyle;

import java.util.List;

/**
 * Markdown渲染
 */
public class MdRender {
    private final MdSectionRender sectionRender = new MdSectionRender();
    private final MdWordRender wordRender = new MdWordRender();

    /**
     * 渲染
     */
    public SpannableStringBuilder render(Context context, List<MdSection> sectionList, BaseMdStyle style) {
        if (sectionList == null || sectionList.isEmpty()) {
            return null;
        }

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        for (int i = 0; i < sectionList.size(); i++) {
            MdSection section = sectionList.get(i);

            MdType type = section.getType();

            if (type == MdType.NORMAL) {
                spanStrBuilder.append(renderNormal(context, section, style));
            } else {
                spanStrBuilder.append(sectionRender.render(context, section, style));
            }

            if (i != sectionList.size() - 1) {
                spanStrBuilder.append("\n");
            }
        }

        return spanStrBuilder;
    }

    private SpannableStringBuilder renderNormal(Context context, MdSection section, BaseMdStyle style) {
        List<MdWord> wordList = section.getWordList();

        if (wordList == null || wordList.isEmpty()) {
            return new SpannableStringBuilder(section.getSrc());
        }

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        for (MdWord word : wordList) {
            spanStrBuilder.append(wordRender.render(context, word, style));
        }

        return spanStrBuilder;
    }
}
