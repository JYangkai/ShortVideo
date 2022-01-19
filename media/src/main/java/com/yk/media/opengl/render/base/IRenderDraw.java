package com.yk.media.opengl.render.base;

public interface IRenderDraw {
    boolean onReadyToDraw();

    void onDrawPre();

    void onClear();

    void onUseProgram();

    void onInitLocation();

    void onBindFbo();

    void onBindVbo();

    void onActiveTexture();

    void onEnableVertexAttributeArray();

    void onSetVertexData();

    void onSetCoordinateData();

    void onSetOtherData();

    void onDrawArrays();

    void onDisableVertexAttributeArray();

    void onUnBind();

    void onDrawPost();
}
