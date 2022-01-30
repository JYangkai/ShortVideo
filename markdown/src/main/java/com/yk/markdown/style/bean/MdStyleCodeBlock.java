package com.yk.markdown.style.bean;

public class MdStyleCodeBlock {
    private int gapWidth;
    private int backgroundColor;
    private int textColor;
    private int textSize;

    public MdStyleCodeBlock(int gapWidth, int backgroundColor, int textColor, int textSize) {
        this.gapWidth = gapWidth;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.textSize = textSize;
    }

    public int getGapWidth() {
        return gapWidth;
    }

    public void setGapWidth(int gapWidth) {
        this.gapWidth = gapWidth;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
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
        return "MdStyleCodeBlock{" +
                "gapWidth=" + gapWidth +
                ", backgroundColor=" + backgroundColor +
                ", textColor=" + textColor +
                ", textSize=" + textSize +
                '}';
    }
}
