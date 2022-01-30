package com.yk.markdown.bean;

public class MdWord extends BaseMdBean {
    private int start;
    private int end;

    public MdWord(MdType type, String src, int start, int end) {
        super(type, src);
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void addOffset(int offset) {
        setStart(getStart() + offset);
        setEnd(getEnd() + offset);
    }

    @Override
    public String toString() {
        return super.toString() +
                ":MdWord{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
