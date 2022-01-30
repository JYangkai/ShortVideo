package com.yk.markdown.style.style;

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

public abstract class BaseMdStyle {
    private MdStyleBase base;
    private MdStyleNormal normal;
    private MdStyleQuote quote;
    private MdStyleCodeBlock codeBlock;
    private MdStyleOrderedList orderedList;
    private MdStyleUnorderedList unorderedList;
    private MdStyleTitle title;
    private MdStyleSeparator separator;
    private MdStyleCode code;
    private MdStyleBoldItalics boldItalics;
    private MdStyleBold bold;
    private MdStyleItalics italics;

    public abstract void init();

    public MdStyleBase getBase() {
        return base;
    }

    public void setBase(MdStyleBase base) {
        this.base = base;
    }

    public MdStyleNormal getNormal() {
        return normal;
    }

    public void setNormal(MdStyleNormal normal) {
        this.normal = normal;
    }

    public MdStyleQuote getQuote() {
        return quote;
    }

    public void setQuote(MdStyleQuote quote) {
        this.quote = quote;
    }

    public MdStyleCodeBlock getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(MdStyleCodeBlock codeBlock) {
        this.codeBlock = codeBlock;
    }

    public MdStyleOrderedList getOrderedList() {
        return orderedList;
    }

    public void setOrderedList(MdStyleOrderedList orderedList) {
        this.orderedList = orderedList;
    }

    public MdStyleUnorderedList getUnorderedList() {
        return unorderedList;
    }

    public void setUnorderedList(MdStyleUnorderedList unorderedList) {
        this.unorderedList = unorderedList;
    }

    public MdStyleTitle getTitle() {
        return title;
    }

    public void setTitle(MdStyleTitle title) {
        this.title = title;
    }

    public MdStyleSeparator getSeparator() {
        return separator;
    }

    public void setSeparator(MdStyleSeparator separator) {
        this.separator = separator;
    }

    public MdStyleCode getCode() {
        return code;
    }

    public void setCode(MdStyleCode code) {
        this.code = code;
    }

    public MdStyleBoldItalics getBoldItalics() {
        return boldItalics;
    }

    public void setBoldItalics(MdStyleBoldItalics boldItalics) {
        this.boldItalics = boldItalics;
    }

    public MdStyleBold getBold() {
        return bold;
    }

    public void setBold(MdStyleBold bold) {
        this.bold = bold;
    }

    public MdStyleItalics getItalics() {
        return italics;
    }

    public void setItalics(MdStyleItalics italics) {
        this.italics = italics;
    }

    @Override
    public String toString() {
        return "BaseMdStyle{" +
                "base=" + base +
                ", normal=" + normal +
                ", quote=" + quote +
                ", codeBlock=" + codeBlock +
                ", orderedList=" + orderedList +
                ", unorderedList=" + unorderedList +
                ", title=" + title +
                ", separator=" + separator +
                ", code=" + code +
                ", boldItalics=" + boldItalics +
                ", bold=" + bold +
                ", italics=" + italics +
                '}';
    }
}
