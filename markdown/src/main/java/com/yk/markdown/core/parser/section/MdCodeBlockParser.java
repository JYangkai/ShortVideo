package com.yk.markdown.core.parser.section;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;

public class MdCodeBlockParser extends BaseMdSectionParser {
    @Override
    public MdType getType() {
        return MdType.CODE_BLOCK;
    }

    @Override
    public boolean matcher(String src) {
        return src.matches("`{3}.*");
    }

    @Override
    public boolean needAppendLastSection(MdSection lastSection, boolean codeBlockClose) {
        return !codeBlockClose;
    }
}
