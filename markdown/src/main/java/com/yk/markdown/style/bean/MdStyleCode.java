package com.yk.markdown.style.bean;

public class MdStyleCode {
    private int backgroundColor;
    private int textColor;
    private int textSize;

    public MdStyleCode(int backgroundColor, int textColor, int textSize) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.textSize = textSize;
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
        return "MdStyleCode{" +
                "backgroundColor=" + backgroundColor +
                ", textColor=" + textColor +
                ", textSize=" + textSize +
                '}';
    }
}
