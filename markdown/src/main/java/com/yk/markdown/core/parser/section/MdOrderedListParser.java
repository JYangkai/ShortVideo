package com.yk.markdown.core.parser.section;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;

public class MdOrderedListParser extends BaseMdSectionParser {
    @Override
    public MdType getType() {
        return MdType.ORDERED_LIST;
    }

    @Override
    public boolean matcher(String src) {
        return src.matches("\\d+\\.\\s.*");
    }

    @Override
    public boolean needAppendLastSection(MdSection lastSection, boolean codeBlockClose) {
        return !codeBlockClose;
    }
}
