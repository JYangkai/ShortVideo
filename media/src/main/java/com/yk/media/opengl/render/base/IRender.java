package com.yk.media.opengl.render.base;

public interface IRender {

    void onCreate();

    void onChange(int width, int height);

    void onDraw(int textureId);

    void onRelease();

}
