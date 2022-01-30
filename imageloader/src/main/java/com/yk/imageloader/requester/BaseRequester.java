package com.yk.imageloader.requester;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.yk.imageloader.R;
import com.yk.imageloader.cache.ActiveCache;
import com.yk.imageloader.cache.DiskCache;
import com.yk.imageloader.cache.MemoryCache;
import com.yk.imageloader.fragment.OnLifeCircleListener;
import com.yk.imageloader.manager.ImageManager;
import com.yk.imageloader.utils.KeyUtils;

public abstract class BaseRequester implements IRequester, OnLifeCircleListener {
    private static final String TAG = "BaseRequester";

    private final ActiveCache activeCache;

    private String path;
    private String key;
    private ImageView iv;

    public BaseRequester() {
        activeCache = new ActiveCache();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        activeCache.recycle();
        DiskCache.getInstance().flush();
    }

    @Override
    public IRequester load(String path) {
        this.path = path;
        this.key = KeyUtils.hashKey(path);
        return this;
    }

    @Override
    public void into(ImageView iv) {
        this.iv = iv;
        iv.setTag(key);
        iv.setImageResource(R.color.color_withe);

        Bitmap bitmap = getCache();
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        } else {
            ImageManager.getInstance().executeIo(path, key, iv);
        }
    }

    public Bitmap getCache() {
        // 活动缓存
        Bitmap bitmap = activeCache.get(key);
        if (bitmap != null) {
            Log.d(TAG, "getCache: 活动缓存:" + key);
            return bitmap;
        }

        // 内存缓存
        bitmap = MemoryCache.getInstance().get(key);
        if (bitmap != null) {
            Log.d(TAG, "getCache: 内存缓存:" + key);
            activeCache.add(key, bitmap);
            MemoryCache.getInstance().remove(key);
            return bitmap;
        }

        // 磁盘缓存
        bitmap = DiskCache.getInstance().get(key);
        if (bitmap != null) {
            Log.d(TAG, "getCache: 磁盘缓存:" + key);
            activeCache.add(key, bitmap);
            return bitmap;
        }

        return null;
    }

    public ActiveCache getActiveCache() {
        return activeCache;
    }

    public String getPath() {
        return path;
    }

    public String getKey() {
        return key;
    }

    public ImageView getIv() {
        return iv;
    }
}
