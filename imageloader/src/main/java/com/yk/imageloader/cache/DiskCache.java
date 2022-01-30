package com.yk.imageloader.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

public class DiskCache {

    private DiskLruCache cache;

    private static volatile DiskCache instance;

    private DiskCache() {
    }

    public static DiskCache getInstance() {
        if (instance == null) {
            synchronized (DiskCache.class) {
                if (instance == null) {
                    instance = new DiskCache();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        if (cache != null) {
            return;
        }
        try {
            File file = getDiskLruCacheDir(context, "bitmap");
            if (!file.exists()) {
                file.mkdirs();
            }
            cache = DiskLruCache.open(file, getAppVersion(context), 1, 100 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap get(String key) {
        if (cache == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = cache.get(key);
            if (snapshot == null) {
                return null;
            }
            bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void add(String key, Bitmap bitmap) {
        if (cache == null) {
            return;
        }
        if (get(key) != null) {
            return;
        }
        try {
            DiskLruCache.Editor editor = cache.edit(key);
            if (editor == null) {
                return;
            }
            boolean success = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, editor.newOutputStream(0));
            if (success) {
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        if (cache == null) {
            return;
        }
        try {
            cache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskLruCacheDir(Context context, String uniqueName) {
        String path = context.getExternalFilesDir(uniqueName).getPath() + File.separator;
        return new File(path);
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
