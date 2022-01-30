package com.yk.markdown.core.parser.word;

import com.yk.markdown.bean.MdType;

public class MdBoldParser extends BaseMdWordParser {
    @Override
    public MdType getType() {
        return MdType.BOLD;
    }

    @Override
    public String getRegex() {
        return "[*]{2}[^*]+[*]{2}";
    }
}
