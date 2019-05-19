package com.example.elf.utils.imageloader.loader.strategy.cache;

import android.graphics.Bitmap;

import java.io.IOException;

import static com.example.elf.utils.imageloader.loader.ConcreteLoadMethod.loadBitmapFromHttp;


public class NetCacheStrategy implements LoadStrategy {
    @Override
    public Bitmap load(String url, int reqWidth, int reqHeight) throws IOException {
        return loadBitmapFromHttp(url, reqWidth, reqHeight);
    }
}
