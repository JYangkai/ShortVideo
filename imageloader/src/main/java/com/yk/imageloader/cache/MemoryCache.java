package com.yk.imageloader.cache;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

public class MemoryCache {

    private final LruCache<String, Bitmap> cache;

    private static volatile MemoryCache instance;

    private MemoryCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        cache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public static MemoryCache getInstance() {
        if (instance == null) {
            synchronized (MemoryCache.class) {
                if (instance == null) {
                    instance = new MemoryCache();
                }
            }
        }
        return instance;
    }

    public Bitmap get(String key) {
        return cache.get(key);
    }

    public void add(String key, Bitmap bitmap) {
        if (get(key) != null) {
            return;
        }
        cache.put(key, bitmap);
    }

    public void remove(String key) {
        cache.remove(key);
    }

}
