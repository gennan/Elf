package com.example.elf.utils.imageloader.loader.strategy.cache;

import android.graphics.Bitmap;

import java.io.IOException;

public interface LoadStrategy {
    Bitmap load(String url, int reqWidth, int reqHeight) throws IOException;
}
