package com.yk.media.opengl.render.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengl.render.base.BaseRender;

public class GaussianRender extends BaseRender {
    private final SubRender horizontal;
    private final SubRender vertical;

    public GaussianRender(Context context) {
        super(context);
        horizontal = new SubRender(context);
        vertical = new SubRender(context);

        horizontal.setRadius(10);
        vertical.setRadius(10);

        horizontal.setOffset(5, 0);
        vertical.setOffset(0, 5);

        horizontal.bindFbo(true);
        vertical.bindFbo(true);
    }

    @Override
    public void onCreate() {
        horizontal.onCreate();
        vertical.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        horizontal.onChange(width, height);
        vertical.onChange(width, height);
    }

    @Override
    public void onDraw(int textureId) {
        horizontal.onDraw(textureId);
        vertical.onDraw(horizontal.getFboTextureId());
    }

    @Override
    public int getFboTextureId() {
        return vertical.getFboTextureId();
    }

    private static class SubRender extends BaseRender {

        private int radius = 0;
        private float offsetX = 0;
        private float offsetY = 0;

        private int uBlurRadiusLocation;
        private int uBlurOffsetLocation;

        public SubRender(Context context) {
            super(context,
                    "render/filter/bilateral/vertex.frag",
                    "render/filter/bilateral/frag.frag");
        }

        @Override
        public void onInitLocation() {
            super.onInitLocation();
            uBlurRadiusLocation = GLES20.glGetUniformLocation(program, "uBlurRadius");
            uBlurOffsetLocation = GLES20.glGetUniformLocation(program, "uBlurOffset");
        }

        @Override
        public void onSetOtherData() {
            super.onSetOtherData();
            GLES20.glUniform1i(uBlurRadiusLocation, radius);
            GLES20.glUniform2f(uBlurOffsetLocation, offsetX / width, offsetY / height);
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public void setOffset(float x, float y) {
            offsetX = x;
            offsetY = y;
        }
    }
}
