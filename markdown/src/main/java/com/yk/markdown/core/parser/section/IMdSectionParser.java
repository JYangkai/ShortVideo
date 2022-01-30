package com.yk.markdown.core.parser.section;

import com.yk.markdown.bean.MdSection;

import java.util.List;

/**
 * section解析接口
 */
public interface IMdSectionParser {
    /**
     * 解析方法
     *
     * @param sectionList 外部传入section list
     * @param src         源文本（一般是解析行）
     * @return 解析结果
     */
    boolean parser(List<MdSection> sectionList, String src);
}
