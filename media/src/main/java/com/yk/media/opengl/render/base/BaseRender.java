package com.yk.media.opengl.render.base;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengl.utils.OpenGLUtils;

import java.nio.FloatBuffer;

public class BaseRender implements IRender, IRenderCreate, IRenderChange, IRenderDraw {
    public final Context context;

    public FloatBuffer vertexBuffer;
    public FloatBuffer coordinateBuffer;

    public int vertexShader;
    public int fragShader;
    public int program;

    public int textureId;
    public int fboTextureId;
    public int fboId;
    public int vboId;

    public int width;
    public int height;

    public boolean isBindFbo = false;
    public boolean isCreate = false;
    public boolean isChange = false;

    public int aPosLocation;
    public int aCoordinateLocation;
    public int uSamplerLocation;

    private final String vertexFilename;
    private final String fragFilename;

    public BaseRender(Context context) {
        this(context, "render/base/base/vertex.frag", "render/base/base/frag.frag");
    }

    public BaseRender(Context context, String vertexFilename, String fragFilename) {
        this.context = context;
        this.vertexFilename = vertexFilename;
        this.fragFilename = fragFilename;
    }

    @Override
    public void onCreate() {
        if (isCreate) {
            return;
        }
        onCreatePre();
        onClearColor();
        onInitBlend();
        onInitVertexBuffer();
        onInitCoordinateBuffer();
        onInitVbo();
        onInitVertexCode();
        onInitFragCode();
        onInitProgram();
        onCreatePost();
        isCreate = true;
    }

    @Override
    public void onChange(int width, int height) {
        if (isChange) {
            return;
        }
        onChangePre();
        onInitSize(width, height);
        onViewport();
        onInitFbo();
        onChangePost();
        isChange = true;
    }

    @Override
    public void onDraw(int textureId) {
        setTextureId(textureId);
        if (!onReadyToDraw()) {
            return;
        }
        onDrawPre();
        onClear();
        onUseProgram();
        onInitLocation();
        onBindFbo();
        onBindVbo();
        onActiveTexture();
        onEnableVertexAttributeArray();
        onSetVertexData();
        onSetCoordinateData();
        onSetOtherData();
        onDrawArrays();
        onDisableVertexAttributeArray();
        onUnBind();
        onDrawPost();
    }

    @Override
    public void onRelease() {
        onDeleteProgram(program);
        onDeleteShader(vertexShader);
        onDeleteShader(fragShader);
        onDeleteTexture(textureId);
        onDeleteTexture(fboTextureId);
        onDeleteFbo(fboId);
        onDeleteVbo(vboId);
    }

    @Override
    public void onCreatePre() {

    }

    @Override
    public void onClearColor() {
        GLES20.glClearColor(0, 0, 0, 1);
    }

    @Override
    public boolean onEnableBlend() {
        return false;
    }

    @Override
    public void onInitBlend() {
        if (!onEnableBlend()) {
            return;
        }
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onInitVertexBuffer() {
        vertexBuffer = OpenGLUtils.getSquareVertexBuffer();
    }

    @Override
    public void onInitCoordinateBuffer() {
        if (isBindFbo) {
            coordinateBuffer = OpenGLUtils.getSquareCoordinateReverseBuffer();
        } else {
            coordinateBuffer = OpenGLUtils.getSquareCoordinateBuffer();
        }
    }

    @Override
    public void onInitVbo() {
        vboId = OpenGLUtils.getVbo(vertexBuffer, coordinateBuffer);
    }

    @Override
    public void onInitVertexCode() {
        String vertexCode = OpenGLUtils.getShaderCode(context, vertexFilename);
        vertexShader = OpenGLUtils.loadVertexShare(vertexCode);
    }

    @Override
    public void onInitFragCode() {
        String fragCode = OpenGLUtils.getShaderCode(context, fragFilename);
        fragShader = OpenGLUtils.loadFragShare(fragCode);
    }

    @Override
    public void onInitProgram() {
        program = OpenGLUtils.linkProgram(vertexShader, fragShader);
    }

    @Override
    public void onCreatePost() {

    }

    @Override
    public void onChangePre() {

    }

    @Override
    public void onInitSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onViewport() {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onInitFbo() {
        if (!isBindFbo) {
            return;
        }
        int[] fboData = OpenGLUtils.getFbo(width, height);
        fboId = fboData[0];
        fboTextureId = fboData[1];
    }

    @Override
    public void onChangePost() {

    }

    @Override
    public boolean onReadyToDraw() {
        return true;
    }

    @Override
    public void onDrawPre() {

    }

    @Override
    public void onClear() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onUseProgram() {
        GLES20.glUseProgram(program);
    }

    @Override
    public void onInitLocation() {
        aPosLocation = GLES20.glGetAttribLocation(program, "aPos");
        aCoordinateLocation = GLES20.glGetAttribLocation(program, "aCoordinate");
        uSamplerLocation = GLES20.glGetUniformLocation(program, "uSampler");
    }

    @Override
    public void onBindFbo() {
        if (!isBindFbo) {
            return;
        }
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, fboTextureId, 0);
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onBindVbo() {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);
    }

    @Override
    public void onActiveTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uSamplerLocation, 0);
    }

    @Override
    public void onEnableVertexAttributeArray() {
        GLES20.glEnableVertexAttribArray(aPosLocation);
        GLES20.glEnableVertexAttribArray(aCoordinateLocation);
    }

    @Override
    public void onSetVertexData() {
        GLES20.glVertexAttribPointer(aPosLocation, 2, GLES20.GL_FLOAT, false, 2 * 4, 0);
    }

    @Override
    public void onSetCoordinateData() {
        GLES20.glVertexAttribPointer(aCoordinateLocation, 2, GLES20.GL_FLOAT, false, 2 * 4, vertexBuffer.limit() * 4);
    }

    @Override
    public void onSetOtherData() {

    }

    @Override
    public void onDrawArrays() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void onDisableVertexAttributeArray() {
        GLES20.glDisableVertexAttribArray(aPosLocation);
        GLES20.glDisableVertexAttribArray(aCoordinateLocation);
    }

    @Override
    public void onUnBind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void onDrawPost() {

    }

    public void onDeleteProgram(int program) {
        GLES20.glDeleteProgram(program);
    }

    public void onDeleteShader(int shader) {
        GLES20.glDeleteShader(shader);
    }

    public void onDeleteTextures(int[] textureIds) {
        GLES20.glDeleteTextures(1, textureIds, 0);
    }

    public void onDeleteTexture(int textureId) {
        GLES20.glDeleteTextures(1, new int[]{textureId}, 0);
    }

    public void onDeleteFbo(int fboId) {
        GLES20.glDeleteFramebuffers(1, new int[]{fboId}, 0);
    }

    public void onDeleteVbo(int vboId) {
        GLES20.glDeleteBuffers(1, new int[]{vboId}, 0);
    }

    public int getFboTextureId() {
        return fboTextureId;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public void bindFbo(boolean isBindFbo) {
        this.isBindFbo = isBindFbo;
    }
}
