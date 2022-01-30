package com.yk.markdown.core.parser.word;

import com.yk.markdown.bean.MdWord;

import java.util.List;

/**
 * word解析接口
 */
public interface IMdWordParser {
    /**
     * 解析方法
     *
     * @param src    源文本（一般是normal section）
     * @param offset 偏移距离（在父文本的偏移距离）
     * @return 解析的word list
     */
    List<MdWord> parser(String src, int offset);
}
