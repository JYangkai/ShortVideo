package com.yk.media.opengl.render.base;

public interface IRenderCreate {
    void onCreatePre();

    void onClearColor();

    boolean onEnableBlend();

    void onInitBlend();

    void onInitVertexBuffer();

    void onInitCoordinateBuffer();

    void onInitVbo();

    void onInitVertexCode();

    void onInitFragCode();

    void onInitProgram();

    void onCreatePost();
}
