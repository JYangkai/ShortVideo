package com.yk.markdown.core.parser.word;

import com.yk.markdown.bean.MdType;

public class MdCodeParser extends BaseMdWordParser {
    @Override
    public MdType getType() {
        return MdType.CODE;
    }

    @Override
    public String getRegex() {
        return "[`][^`]+[`]";
    }
}
