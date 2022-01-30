package com.yk.markdown.core.parser.word;

import com.yk.markdown.bean.MdType;

public class MdBoldItalicsParser extends BaseMdWordParser {
    @Override
    public MdType getType() {
        return MdType.BOLD_ITALICS;
    }

    @Override
    public String getRegex() {
        return "[*]{3}[^*]+[*]{3}";
    }
}
