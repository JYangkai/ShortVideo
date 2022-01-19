package com.yk.media.opengl.render.core;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.yk.media.opengl.render.base.BaseRender;
import com.yk.media.opengl.utils.OpenGLUtils;

/**
 * SurfaceTexture使用callback的方式渲染
 */
public class OesCallbackRender extends BaseRender {

    private int oesTextureId;

    private int uMatrixLocation;
    private int uOesMatrixLocation;

    private int oesW = -1;
    private int oesH = -1;

    private float[] mMVPMatrix = new float[16];
    private float[] mOesMatrix = {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    private boolean isReadyToDraw = false;

    private SurfaceTexture surfaceTexture;

    private OnOesListener onOesListener;

    public OesCallbackRender(Context context) {
        super(context, "render/base/oes/vertex.frag", "render/base/oes/frag.frag");
    }

    @Override
    public void onInitCoordinateBuffer() {
        coordinateBuffer = OpenGLUtils.getSquareCoordinateBuffer();
    }

    @Override
    public void onCreatePost() {
        super.onCreatePost();
        oesTextureId = OpenGLUtils.getOesTexture();
    }

    public void onDrawSelf() {
        onDraw(oesTextureId);
    }

    @Override
    public boolean onReadyToDraw() {
        if (!isReadyToDraw) {
            if (onOesListener == null) {
                return false;
            }
            if (surfaceTexture != null) {
                surfaceTexture.release();
                surfaceTexture = null;
            }
            surfaceTexture = new SurfaceTexture(oesTextureId);
            onOesListener.onOes(surfaceTexture);
            isReadyToDraw = true;
        }

        return oesW != -1 && oesH != -1;
    }

    @Override
    public void onDrawPre() {
        super.onDrawPre();
        mMVPMatrix = OpenGLUtils.getMatrix(width, height, oesW, oesH);

        surfaceTexture.updateTexImage();

        float[] oesMatrix = new float[16];
        surfaceTexture.getTransformMatrix(oesMatrix);
        if (!OpenGLUtils.isIdentityM(oesMatrix)) {
            mOesMatrix = oesMatrix;
        }
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uMatrixLocation = GLES20.glGetUniformLocation(program, "uMatrix");
        uOesMatrixLocation = GLES20.glGetUniformLocation(program, "uOesMatrix");
    }

    @Override
    public void onActiveTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        GLES20.glUniform1i(uSamplerLocation, 0);
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(uOesMatrixLocation, 1, false, mOesMatrix, 0);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        onDeleteTexture(oesTextureId);
    }

    public void setOesSize(int width, int height) {
        oesW = width;
        oesH = height;
    }

    public void setOnOesListener(OnOesListener onOesListener) {
        this.onOesListener = onOesListener;
        isReadyToDraw = false;
    }

    public interface OnOesListener {
        void onOes(SurfaceTexture surfaceTexture);
    }
}
