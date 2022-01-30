package com.yk.markdown.core.parser.word;

import com.yk.markdown.bean.MdType;

public class MdItalicsParser extends BaseMdWordParser {
    @Override
    public MdType getType() {
        return MdType.ITALICS;
    }

    @Override
    public String getRegex() {
        return "[*][^*]+[*]";
    }
}
