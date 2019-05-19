package com.example.elf.utils.imageloader.loader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

public class ImageResizer {
    private static final String TAG = "ImageResizer";

    public ImageResizer() {
    }

    /**
     * 传入Resource的参数然后返回按照一定采样率压缩后的图片
     * 在setImageBitmap时直接调用
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSampledBitmapFromResource(
            Resources res, int resId, int reqWidth, int reqHeight) {
        //获取 BitmapFactory.Options，这里面保存了很多有关Bitmap的设置
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //设置true轻量加载图片信息,只解析宽高
        options.inJustDecodeBounds = true;
        //由于上面设置的true，这里进行轻量加载图片的操作
        BitmapFactory.decodeResource(res, resId, options);
        //计算采样率(inSampleSize)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //设置false正常加载图片
        options.inJustDecodeBounds = false;
        //设置为false后返回正常加载的Bitmap图片
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 传入FileDescriptor的参数然后返回按照一定采样率压缩后的图片
     *
     * @param fd
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 就这里的方法与上面的BitmapFactory.decodeResource()方法不同 其他地方都一样
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    /**
     * 计算采样率
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                //宽或高大于预期就将 采样率*=2 进行缩放
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
