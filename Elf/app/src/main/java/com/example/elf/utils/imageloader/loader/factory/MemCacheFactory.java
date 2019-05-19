package com.example.elf.utils.imageloader.loader.factory;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

public class MemCacheFactory {
    private MemCacheFactory() {

    }

    public static LruCache createMemCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // cacheSize 一般为可用最大内存的 1/8.
        int cacheSize = maxMemory / 8;
        return new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }
}
