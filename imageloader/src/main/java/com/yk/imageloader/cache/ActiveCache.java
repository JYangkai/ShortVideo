package com.yk.imageloader.cache;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class ActiveCache {
    private final Map<String, Bitmap> cache;

    public ActiveCache() {
        cache = new HashMap<>();
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

    public void recycle() {
        for (String key : cache.keySet()) {
            Bitmap bitmap = cache.get(key);
            MemoryCache.getInstance().add(key, bitmap);
        }
        cache.clear();
    }
}
