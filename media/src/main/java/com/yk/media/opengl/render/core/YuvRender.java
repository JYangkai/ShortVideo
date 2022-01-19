package com.yk.media.opengl.render.core;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengl.render.base.BaseRender;
import com.yk.media.opengl.utils.OpenGLUtils;

public class YuvRender extends BaseRender {

    private int yTextureId = -1;
    private int uTextureId = -1;
    private int vTextureId = -1;

    private byte[] yData;
    private byte[] uData;
    private byte[] vData;

    private int uYSamplerLocation;
    private int uUSamplerLocation;
    private int uVSamplerLocation;

    private int yuvWidth = -1;
    private int yuvHeight = -1;

    private float[] matrix;
    private int uMatrixLocation;

    public YuvRender(Context context) {
        super(context);
    }

    @Override
    public boolean onReadyToDraw() {
        return yData != null && uData != null && vData != null && yuvWidth != -1 && yuvHeight != -1;
    }

    @Override
    public void onDrawPre() {
        super.onDrawPre();

        if (yTextureId != -1) {
            onDeleteTexture(yTextureId);
        }

        if (uTextureId != -1) {
            onDeleteTexture(uTextureId);
        }

        if (vTextureId != -1) {
            onDeleteTexture(vTextureId);
        }

        yTextureId = OpenGLUtils.getYuvTexture(yData, yuvWidth, yuvHeight);
        uTextureId = OpenGLUtils.getYuvTexture(uData, yuvWidth / 2, yuvHeight / 2);
        vTextureId = OpenGLUtils.getYuvTexture(vData, yuvWidth / 2, yuvHeight / 2);
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uYSamplerLocation = GLES20.glGetUniformLocation(program, "uYSampler");
        uUSamplerLocation = GLES20.glGetUniformLocation(program, "uUSampler");
        uVSamplerLocation = GLES20.glGetUniformLocation(program, "uVSampler");
        uMatrixLocation = GLES20.glGetUniformLocation(program, "uMatrix");
    }

    @Override
    public void onActiveTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, yTextureId);
        GLES20.glUniform1i(uSamplerLocation, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, uTextureId);
        GLES20.glUniform1i(uSamplerLocation, 1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, vTextureId);
        GLES20.glUniform1i(uSamplerLocation, 2);
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        float[] matrix = OpenGLUtils.getMatrix(width, height, yuvWidth, yuvHeight);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public void setNv12Data(byte[] data, int width, int height) {
        yuvWidth = width;
        yuvHeight = height;

        yData = new byte[width * height];
        uData = new byte[width * height / 4];
        vData = new byte[width * height / 4];

        System.arraycopy(data, 0, yData, 0, width * height);

        for (int i = width * height, j = 0; i < data.length; i++, j++) {
            uData[j] = data[i++];
            vData[j] = data[i];
        }
    }
}
