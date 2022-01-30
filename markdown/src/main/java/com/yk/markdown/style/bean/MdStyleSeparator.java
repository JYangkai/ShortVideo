package com.yk.markdown.style.bean;

public class MdStyleSeparator {
    private int color;
    private int size;

    public MdStyleSeparator(int color, int size) {
        this.color = color;
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "MdStyleSeparator{" +
                "color=" + color +
                ", size=" + size +
                '}';
    }
}
