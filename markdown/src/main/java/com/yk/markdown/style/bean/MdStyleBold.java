package com.yk.markdown.style.bean;

public class MdStyleBold {
    private int textColor;
    private int textSize;

    public MdStyleBold(int textColor, int textSize) {
        this.textColor = textColor;
        this.textSize = textSize;
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
        return "MdStyleBoldItalics{" +
                "textColor=" + textColor +
                ", textSize=" + textSize +
                '}';
    }
}
