package com.yk.markdown.style.style;

import android.graphics.Color;

import com.yk.markdown.style.bean.MdStyleBase;
import com.yk.markdown.style.bean.MdStyleBold;
import com.yk.markdown.style.bean.MdStyleBoldItalics;
import com.yk.markdown.style.bean.MdStyleCode;
import com.yk.markdown.style.bean.MdStyleCodeBlock;
import com.yk.markdown.style.bean.MdStyleItalics;
import com.yk.markdown.style.bean.MdStyleNormal;
import com.yk.markdown.style.bean.MdStyleOrderedList;
import com.yk.markdown.style.bean.MdStyleQuote;
import com.yk.markdown.style.bean.MdStyleSeparator;
import com.yk.markdown.style.bean.MdStyleTitle;
import com.yk.markdown.style.bean.MdStyleUnorderedList;

public class StandardMdStyle extends BaseMdStyle {
    @Override
    public void init() {
        // 基础
        setBase(
                new MdStyleBase(
                        Color.BLACK,
                        64
                )
        );

        // 普通
        setNormal(
                new MdStyleNormal(
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );

        // 引用
        setQuote(
                new MdStyleQuote(
                        Color.parseColor("#30888888"),
                        20,
                        30,
                        Color.parseColor("#A0000000"),
                        getBase().getTextSize()
                )
        );

        // 代码块
        setCodeBlock(
                new MdStyleCodeBlock(
                        30,
                        Color.parseColor("#30888888"),
                        Color.parseColor("#A0000000"),
                        getBase().getTextSize()
                )
        );

        // 有序列表
        setOrderedList(
                new MdStyleOrderedList(
                        Color.BLACK,
                        56,
                        30,
                        30,
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );

        // 无序列表
        setUnorderedList(
                new MdStyleUnorderedList(
                        Color.BLACK,
                        8,
                        30,
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );

        // 标题
        setTitle(
                new MdStyleTitle(
                        getBase().getTextColor(),
                        getBase().getTextSize() + 16 * 4,
                        getBase().getTextSize() + 16 * 3,
                        getBase().getTextSize() + 16 * 2,
                        getBase().getTextSize() + 16,
                        getBase().getTextSize()
                )
        );

        // 分隔符
        setSeparator(
                new MdStyleSeparator(
                        Color.parseColor("#30888888"),
                        2
                )
        );

        // 代码
        setCode(
                new MdStyleCode(
                        Color.parseColor("#10FF0000"),
                        Color.parseColor("#A0FF0000"),
                        getBase().getTextSize()
                )
        );

        // 粗斜体
        setBoldItalics(
                new MdStyleBoldItalics(
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );

        // 粗体
        setBold(
                new MdStyleBold(
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );

        // 斜体
        setItalics(
                new MdStyleItalics(
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );
    }
}
