package com.yk.markdown.style.bean;

public class MdStyleOrderedList {
    private int indexColor;
    private int indexSize;
    private int indexWidth;
    private int gapWidth;
    private int textColor;
    private int textSize;

    public MdStyleOrderedList(int indexColor, int indexSize, int indexWidth, int gapWidth, int textColor, int textSize) {
        this.indexColor = indexColor;
        this.indexSize = indexSize;
        this.indexWidth = indexWidth;
        this.gapWidth = gapWidth;
        this.textColor = textColor;
        this.textSize = textSize;
    }

    public int getIndexColor() {
        return indexColor;
    }

    public void setIndexColor(int indexColor) {
        this.indexColor = indexColor;
    }

    public int getIndexSize() {
        return indexSize;
    }

    public void setIndexSize(int indexSize) {
        this.indexSize = indexSize;
    }

    public int getIndexWidth() {
        return indexWidth;
    }

    public void setIndexWidth(int indexWidth) {
        this.indexWidth = indexWidth;
    }

    public int getGapWidth() {
        return gapWidth;
    }

    public void setGapWidth(int gapWidth) {
        this.gapWidth = gapWidth;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public String toString() {
        return "MdStyleOrderedList{" +
                "indexColor=" + indexColor +
                ", indexSize=" + indexSize +
                ", indexWidth=" + indexWidth +
                ", gapWidth=" + gapWidth +
                ", textColor=" + textColor +
                ", textSize=" + textSize +
                '}';
    }
}
