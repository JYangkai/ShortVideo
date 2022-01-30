package com.yk.markdown.style.bean;

public class MdStyleQuote {
    private int stripeColor;
    private int stripeWidth;
    private int gapWidth;
    private int textColor;
    private int textSize;

    public MdStyleQuote(int stripeColor, int stripeWidth, int gapWidth, int textColor, int textSize) {
        this.stripeColor = stripeColor;
        this.stripeWidth = stripeWidth;
        this.gapWidth = gapWidth;
        this.textColor = textColor;
        this.textSize = textSize;
    }

    public int getStripeColor() {
        return stripeColor;
    }

    public void setStripeColor(int stripeColor) {
        this.stripeColor = stripeColor;
    }

    public int getStripeWidth() {
        return stripeWidth;
    }

    public void setStripeWidth(int stripeWidth) {
        this.stripeWidth = stripeWidth;
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
        return "MdStyleQuote{" +
                "stripeColor=" + stripeColor +
                ", stripeWidth=" + stripeWidth +
                ", gapWidth=" + gapWidth +
                ", textColor=" + textColor +
                ", textSize=" + textSize +
                '}';
    }
}
