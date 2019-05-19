package com.example.elf.utils.imageloader.loader;

import android.widget.ImageView;

public class LoaderOptions {
    private String uri;//图片的uri
    private String whereToLoadFrom;//从哪里加载图片 有内存 磁盘 网络三个可以选择
    private ImageView imageView;//需要加载图片的控件
    private int reqWidth;//图片需要压缩成什么样的宽度
    private int reqHeight;//图片需要压缩成什么样的高度

    public LoaderOptions() {
    }

    public LoaderOptions(LoaderOptions origin) {
        this.uri = origin.uri;
        this.whereToLoadFrom = origin.whereToLoadFrom;
        this.imageView = origin.imageView;
        this.reqWidth = origin.reqWidth;
        this.reqHeight = origin.reqHeight;
    }

    public String getUri() {
        return uri;
    }


    public String getWhereToLoadFrom() {
        return whereToLoadFrom;
    }

    public ImageView getImageView() {
        return imageView;
    }


    public int getReqWidth() {
        return reqWidth;
    }

    public int getReqHeight() {
        return reqHeight;
    }

    public static class ImageLoaderBuilder {
        private LoaderOptions target;

        public ImageLoaderBuilder() {
            target = new LoaderOptions();
        }

        public ImageLoaderBuilder uri(String uri) {
            target.uri = uri;
            return this;
        }


        public ImageLoaderBuilder whereToLoadFrom(String whereToLoadFrom) {
            target.whereToLoadFrom = whereToLoadFrom;
            return this;
        }

        public ImageLoaderBuilder imageView(ImageView imageView) {
            target.imageView = imageView;
            return this;
        }

        public ImageLoaderBuilder reqWidth(int reqWidth) {
            target.reqWidth = reqWidth;
            return this;
        }

        public ImageLoaderBuilder reqHeight(int reqHeight) {
            target.reqHeight = reqHeight;
            return this;
        }

        public LoaderOptions build() {
            return new LoaderOptions(target);
        }
    }


}
