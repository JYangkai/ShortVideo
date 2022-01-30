package com.yk.markdown.core.parser.section;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;

public class MdQuoteParser extends BaseMdSectionParser {
    @Override
    public MdType getType() {
        return MdType.QUOTE;
    }

    @Override
    public boolean matcher(String src) {
        return src.matches(">\\s.*");
    }

    @Override
    public boolean needAppendLastSection(MdSection lastSection, boolean codeBlockClose) {
        return !codeBlockClose;
    }
}
