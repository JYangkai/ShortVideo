package com.yk.markdown.style.bean;

public class MdStyleUnorderedList {
    private int circleColor;
    private int circleRadius;
    private int gapWidth;
    private int textColor;
    private int textSize;

    public MdStyleUnorderedList(int circleColor, int circleRadius, int gapWidth, int textColor, int textSize) {
        this.circleColor = circleColor;
        this.circleRadius = circleRadius;
        this.gapWidth = gapWidth;
        this.textColor = textColor;
        this.textSize = textSize;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
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
        return "MdStyleUnorderedList{" +
                "circleColor=" + circleColor +
                ", circleRadius=" + circleRadius +
                ", gapWidth=" + gapWidth +
                ", textColor=" + textColor +
                ", textSize=" + textSize +
                '}';
    }
}
