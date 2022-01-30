package com.yk.imageloader.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.yk.imageloader.cache.DiskCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageManager {
    private static final String TAG = "ImageManager";

    private final ExecutorService ioService;
    private final Handler uiHandler;

    private static volatile ImageManager instance;

    private ImageManager() {
        ioService = Executors.newFixedThreadPool(5);
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public static ImageManager getInstance() {
        if (instance == null) {
            synchronized (ImageManager.class) {
                if (instance == null) {
                    instance = new ImageManager();
                }
            }
        }
        return instance;
    }

    public void executeIo(String path, String key, ImageView iv) {
        ioService.execute(new IORunnable(path, key, iv));
    }

    public void executeUi(String key, Bitmap bitmap, ImageView iv) {
        uiHandler.post(new UIRunnable(key, bitmap, iv));
    }

    static class IORunnable implements Runnable {
        private final String path;
        private final String key;
        private final ImageView iv;

        public IORunnable(String path, String key, ImageView iv) {
            this.path = path;
            this.key = key;
            this.iv = iv;
        }

        @Override
        public void run() {
            Bitmap bitmap = loadTheBestBitmap(path, iv.getWidth(), iv.getHeight());

            if (bitmap == null) {
                return;
            }

            DiskCache.getInstance().add(key, bitmap);

            ImageManager.getInstance().executeUi(key, bitmap, iv);
        }

        private Bitmap loadTheBestBitmap(String path, int requestW, int requestH) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inSampleSize = calculateInSampleSize(options, requestW, requestH);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(path, options);
        }

        public int calculateInSampleSize(BitmapFactory.Options options,
                                         int reqWidth, int reqHeight) {
            // 源图片的高度和宽度
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                // 计算出实际宽高和目标宽高的比率
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
                // 一定都会大于等于目标的宽和高。
                inSampleSize = Math.min(heightRatio, widthRatio);
            }
            return inSampleSize;
        }
    }

    static class UIRunnable implements Runnable {
        private final String key;
        private final Bitmap bitmap;
        private final ImageView iv;

        public UIRunnable(String key, Bitmap bitmap, ImageView iv) {
            this.key = key;
            this.bitmap = bitmap;
            this.iv = iv;
        }

        @Override
        public void run() {
            String tag = (String) iv.getTag();

            if (!key.equals(tag)) {
                return;
            }

            iv.setImageBitmap(bitmap);

            Log.d(TAG, "run: IO流:" + key);
        }
    }

}
