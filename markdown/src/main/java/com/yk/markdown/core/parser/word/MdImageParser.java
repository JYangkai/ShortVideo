package com.yk.markdown.core.parser.word;

import com.yk.markdown.bean.MdType;

public class MdImageParser extends BaseMdWordParser {
    @Override
    public MdType getType() {
        return MdType.IMAGE;
    }

    @Override
    public String getRegex() {
        return "!\\[.*\\]\\(.*\\)";
    }
}
