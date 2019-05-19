package com.example.elf.utils.imageloader.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class LoaderResult {
    public ImageView imageView;
    public String uri;
    public Bitmap bitmap;

    public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
        this.imageView = imageView;
        this.uri = uri;
        this.bitmap = bitmap;
    }

}
