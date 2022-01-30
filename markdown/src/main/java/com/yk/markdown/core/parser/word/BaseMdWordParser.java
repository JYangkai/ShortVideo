package com.yk.markdown.core.parser.word;

import com.yk.markdown.bean.MdType;
import com.yk.markdown.bean.MdWord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseMdWordParser implements IMdWordParser {
    @Override
    public List<MdWord> parser(String src, int offset) {
        // 该list存储所有的word
        List<MdWord> wordList = new ArrayList<>();

        // 获取解析类
        MdType type = getType();

        if (type == MdType.NORMAL) {
            // 如果是normal类型的word，则直接添加到word list中，并返回
            wordList.add(new MdWord(type, src, offset, offset + src.length()));
            return wordList;
        }

        // 该list存储当前解析类型的word
        List<MdWord> curList = new ArrayList<>();

        // 开始解析
        Pattern pattern = Pattern.compile(getRegex());
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group();

            if (curList.isEmpty()) {
                if (start != 0) {
                    // 如果之前有找到，且当前找到的start不为0，则需要将0到start的字段设置为normal，并添加到word list
                    wordList.add(new MdWord(MdType.NORMAL, src.substring(0, start), 0, start));
                }
            } else {
                // 如果之前又找到，则需要把之前的lastEnd到当前的start之间的字段设置为normal，并添加到word list
                MdWord lastWord = curList.get(curList.size() - 1);
                int lastEnd = lastWord.getEnd();
                wordList.add(new MdWord(MdType.NORMAL, src.substring(lastEnd, start), lastEnd, start));
            }

            // 将当前找到的字段添加到cur list
            curList.add(new MdWord(type, group, start, end));
        }

        if (!curList.isEmpty()) {
            // 如果当前有找到对应的word，则需要判断最后一个word
            MdWord lastWord = curList.get(curList.size() - 1);
            int lastEnd = lastWord.getEnd();
            if (lastEnd != src.length()) {
                // 最后一个word的end不为总字段长度，则需要将end到总字段结尾的这部分字段设置为normal，并添加到word list
                wordList.add(new MdWord(MdType.NORMAL, src.substring(lastEnd), lastEnd, src.length()));
            }
        } else {
            // 如果当前没找到，则将总字段设置为normal，并添加到word list
            wordList.add(new MdWord(MdType.NORMAL, src, 0, src.length()));
        }

        // 将cur list添加到word list
        wordList.addAll(curList);

        for (MdWord word : wordList) {
            // 遍历word list，并添加偏移距离
            word.addOffset(offset);
        }

        // 对word list进行一次排序
        Collections.sort(wordList, new Comparator<MdWord>() {
            @Override
            public int compare(MdWord o1, MdWord o2) {
                return o1.getStart() - o2.getStart();
            }
        });

        return wordList;
    }

    /**
     * 获取Md类型
     */
    public abstract MdType getType();

    /**
     * 获取匹配规则
     */
    public abstract String getRegex();
}
