package com.yk.markdown.style.bean;

public class MdStyleTitle {
    private int textColor;
    private int textSize1;
    private int textSize2;
    private int textSize3;
    private int textSize4;
    private int textSize5;

    public MdStyleTitle(int textColor, int textSize1, int textSize2, int textSize3, int textSize4, int textSize5) {
        this.textColor = textColor;
        this.textSize1 = textSize1;
        this.textSize2 = textSize2;
        this.textSize3 = textSize3;
        this.textSize4 = textSize4;
        this.textSize5 = textSize5;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize1() {
        return textSize1;
    }

    public void setTextSize1(int textSize1) {
        this.textSize1 = textSize1;
    }

    public int getTextSize2() {
        return textSize2;
    }

    public void setTextSize2(int textSize2) {
        this.textSize2 = textSize2;
    }

    public int getTextSize3() {
        return textSize3;
    }

    public void setTextSize3(int textSize3) {
        this.textSize3 = textSize3;
    }

    public int getTextSize4() {
        return textSize4;
    }

    public void setTextSize4(int textSize4) {
        this.textSize4 = textSize4;
    }

    public int getTextSize5() {
        return textSize5;
    }

    public void setTextSize5(int textSize5) {
        this.textSize5 = textSize5;
    }

    @Override
    public String toString() {
        return "MdStyleTitle{" +
                "textColor=" + textColor +
                ", textSize1=" + textSize1 +
                ", textSize2=" + textSize2 +
                ", textSize3=" + textSize3 +
                ", textSize4=" + textSize4 +
                ", textSize5=" + textSize5 +
                '}';
    }
}
