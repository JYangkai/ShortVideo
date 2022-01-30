package com.yk.markdown.bean;

import java.util.List;

public class MdSection extends BaseMdBean {
    private List<MdWord> wordList;

    public MdSection(MdType type, String src, List<MdWord> wordList) {
        super(type, src);
        this.wordList = wordList;
    }

    public List<MdWord> getWordList() {
        return wordList;
    }

    public void setWordList(List<MdWord> wordList) {
        this.wordList = wordList;
    }

    @Override
    public String toString() {
        return super.toString() +
                ":MdSection{" +
                "wordList=" + wordList +
                '}';
    }
}
