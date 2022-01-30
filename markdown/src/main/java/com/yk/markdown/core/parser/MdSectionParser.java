package com.yk.markdown.core.parser;

import android.text.TextUtils;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.core.parser.section.IMdSectionParser;
import com.yk.markdown.core.parser.section.MdCodeBlockParser;
import com.yk.markdown.core.parser.section.MdNormalParser;
import com.yk.markdown.core.parser.section.MdOrderedListParser;
import com.yk.markdown.core.parser.section.MdQuoteParser;
import com.yk.markdown.core.parser.section.MdSeparatorParser;
import com.yk.markdown.core.parser.section.MdTitleParser;
import com.yk.markdown.core.parser.section.MdUnorderedListParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析section
 */
public class MdSectionParser {
    private final IMdSectionParser quoteParser = new MdQuoteParser();
    private final IMdSectionParser codeBlockParser = new MdCodeBlockParser();
    private final IMdSectionParser orderedListParser = new MdOrderedListParser();
    private final IMdSectionParser unorderedListParser = new MdUnorderedListParser();
    private final IMdSectionParser titleParser = new MdTitleParser();
    private final IMdSectionParser separatorParser = new MdSeparatorParser();
    private final IMdSectionParser normalParser = new MdNormalParser();

    /**
     * 解析section
     */
    public List<MdSection> parser(String src) {
        if (TextUtils.isEmpty(src)) {
            // 源文本为空，则直接返回null
            return null;
        }

        // 分行
        String[] srcSplit = src.split("\n");

        if (srcSplit.length == 0) {
            // 分行的数据为空，则直接返回null
            return null;
        }

        List<MdSection> sectionList = new ArrayList<>();

        for (String s : srcSplit) {
            // 遍历所有的行，使用各个parser去解析
            if (quoteParser.parser(sectionList, s)) {
                continue;
            }
            if (codeBlockParser.parser(sectionList, s)) {
                continue;
            }
            if (orderedListParser.parser(sectionList, s)) {
                continue;
            }
            if (unorderedListParser.parser(sectionList, s)) {
                continue;
            }
            if (titleParser.parser(sectionList, s)) {
                continue;
            }
            if (separatorParser.parser(sectionList, s)) {
                continue;
            }

            normalParser.parser(sectionList, s);
        }

        return sectionList;
    }
}
