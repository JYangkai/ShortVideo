package com.yk.markdown.bean;

/**
 * Md基础实体类
 */
public class BaseMdBean {
    /**
     * 类型
     */
    private MdType type;

    /**
     * 源文本
     */
    private String src;

    /**
     * 额外字段
     */
    private String extra;

    public BaseMdBean(MdType type, String src) {
        this.type = type;
        this.src = src;
    }

    public MdType getType() {
        return type;
    }

    public void setType(MdType type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public BaseMdBean appendSrc(String src) {
        setSrc(getSrc() + src);
        return this;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "BaseMdBean{" +
                "type=" + type +
                ", src='" + src + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
