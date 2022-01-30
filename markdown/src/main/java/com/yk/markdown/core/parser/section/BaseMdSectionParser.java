package com.yk.markdown.core.parser.section;

import com.yk.markdown.bean.MdSection;
import com.yk.markdown.bean.MdType;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础解析类
 */
public abstract class BaseMdSectionParser implements IMdSectionParser {
    @Override
    public boolean parser(List<MdSection> sectionList, String src) {
        if (sectionList == null) {
            // 为空表示解析失败
            return false;
        }

        if (!matcher(src)) {
            // 匹配不上，则表示解析失败
            return false;
        }

        // 获取解析类型
        MdType type = getType();

        // 代码块是否关闭
        boolean codeBlockClose = isCodeBlockClose(getCodeBlockCount(sectionList));

        // 上一个section
        MdSection lastSection = null;
        if (sectionList.size() > 0) {
            lastSection = sectionList.get(sectionList.size() - 1);
        }

        if (lastSection == null) {
            // 如果上一个section为空，则直接添加当前section
            sectionList.add(new MdSection(type, src, new ArrayList<>()));
            return true;
        }

        if (needAppendLastSection(lastSection, codeBlockClose)) {
            // 上一个section不为空，且需要添加到上一个section
            lastSection.appendSrc("\n").appendSrc(src);
        } else {
            // 不需要添加到上一个section
            sectionList.add(new MdSection(type, src, new ArrayList<>()));
        }

        return true;
    }

    /**
     * 获取Md类型
     */
    public abstract MdType getType();

    /**
     * 匹配
     */
    public abstract boolean matcher(String src);

    /**
     * 需要追加到上一个section
     */
    public abstract boolean needAppendLastSection(MdSection lastSection, boolean codeBlockClose);

    /**
     * 代码块关闭
     */
    private static boolean isCodeBlockClose(int codeBlockCount) {
        return codeBlockCount % 2 == 0;
    }

    /**
     * 获取代码块标识数量
     */
    private static int getCodeBlockCount(List<MdSection> sectionList) {
        if (sectionList == null || sectionList.isEmpty()) {
            return 0;
        }

        int count = 0;

        for (MdSection section : sectionList) {
            String[] srcSplit = section.getSrc().split("\n");

            if (srcSplit.length == 0) {
                continue;
            }

            for (String src : srcSplit) {
                if (!src.matches("`{3}.*")) {
                    continue;
                }
                count++;
            }
        }

        return count;
    }
}
