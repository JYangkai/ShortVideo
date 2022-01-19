package com.yk.media.opengl.render.base;

public interface IRenderChange {
    void onChangePre();

    void onInitSize(int width, int height);

    void onViewport();

    void onInitFbo();

    void onChangePost();
}