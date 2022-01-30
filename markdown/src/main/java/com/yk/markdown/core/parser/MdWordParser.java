package com.yk.markdown.core.parser;

import android.text.TextUtils;

import com.yk.markdown.bean.MdType;
import com.yk.markdown.bean.MdWord;
import com.yk.markdown.core.parser.word.IMdWordParser;
import com.yk.markdown.core.parser.word.MdBoldItalicsParser;
import com.yk.markdown.core.parser.word.MdBoldParser;
import com.yk.markdown.core.parser.word.MdCodeParser;
import com.yk.markdown.core.parser.word.MdImageParser;
import com.yk.markdown.core.parser.word.MdItalicsParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析word
 */
public class MdWordParser {
    private final IMdWordParser codeParser = new MdCodeParser();
    private final IMdWordParser boldItalicsParser = new MdBoldItalicsParser();
    private final IMdWordParser boldParser = new MdBoldParser();
    private final IMdWordParser italicsParser = new MdItalicsParser();
    private final IMdWordParser imageParser = new MdImageParser();

    /**
     * 解析word
     */
    public List<MdWord> parser(String src) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }

        List<MdWord> wordList = new ArrayList<>();
        // 优先解析代码
        parserCode(wordList, src, 0);

        return wordList;
    }

    /**
     * 解析代码
     */
    private void parserCode(List<MdWord> wordList, String src, int offset) {
        List<MdWord> codeList = codeParser.parser(src, offset);

        for (MdWord word : codeList) {
            MdType type = word.getType();
            if (type == MdType.NORMAL) {
                parserBoldItalics(wordList, word.getSrc(), word.getStart());
            } else {
                wordList.add(word);
            }
        }
    }

    /**
     * 解析粗斜体
     */
    private void parserBoldItalics(List<MdWord> wordList, String src, int offset) {
        List<MdWord> boldItalicsList = boldItalicsParser.parser(src, offset);

        for (MdWord word : boldItalicsList) {
            MdType type = word.getType();
            if (type == MdType.NORMAL) {
                parserBold(wordList, word.getSrc(), word.getStart());
            } else {
                wordList.add(word);
            }
        }
    }

    /**
     * 解析粗体
     */
    private void parserBold(List<MdWord> wordList, String src, int offset) {
        List<MdWord> boldList = boldParser.parser(src, offset);

        for (MdWord word : boldList) {
            MdType type = word.getType();
            if (type == MdType.NORMAL) {
                parserItalics(wordList, word.getSrc(), word.getStart());
            } else {
                wordList.add(word);
            }
        }
    }

    /**
     * 解析斜体
     */
    private void parserItalics(List<MdWord> wordList, String src, int offset) {
        List<MdWord> italicsList = italicsParser.parser(src, offset);

        for (MdWord word : italicsList) {
            MdType type = word.getType();
            if (type == MdType.NORMAL) {
                parserImage(wordList, word.getSrc(), word.getStart());
            } else {
                wordList.add(word);
            }
        }
    }

    /**
     * 解析图片
     */
    private void parserImage(List<MdWord> wordList, String src, int offset) {
        List<MdWord> imageList = imageParser.parser(src, offset);

        wordList.addAll(imageList);
    }
}
