package com.example.elf.utils.imageloader.loader.strategy.cache;

import android.graphics.Bitmap;

import java.io.IOException;

public class CacheManager {
    LoadStrategy mStrategy;

    public void setStrategy(LoadStrategy strategy) {
        mStrategy = strategy;
    }

    public Bitmap load(String url, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        try {
            bitmap = mStrategy.load(url, reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
