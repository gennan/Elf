package com.example.elf.utils.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;


import com.example.elf.utils.imageloader.diskLruCache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.elf.utils.imageloader.MD5Arithmetic.hashKeyFormUrl;
import static com.example.elf.utils.imageloader.loader.ImageLoader.DISK_CACHE_INDEX;
import static com.example.elf.utils.imageloader.loader.ImageLoader.IO_BUFFER_SIZE;
import static com.example.elf.utils.imageloader.loader.ImageLoader.sDiskLruCache;
import static com.example.elf.utils.imageloader.loader.ImageLoader.sMemoryCache;


public class ConcreteLoadMethod {
    private static final String TAG = "ConcreteLoadMethod";
    private static ImageResizer sImageResizer = new ImageResizer();

    /**
     * 从内存中加载图片
     * Android开发艺术探索中没有对从内存加载的图片进行压缩
     * 目前还没想出来怎么在这里压缩一下图片
     *
     * @param url
     * @return
     */
    public static Bitmap loadBitmapFromMemCache(String url) {
        final String key = hashKeyFormUrl(url);
        Bitmap bitmap = getBitmapFromMemCache(key);
        return bitmap;
    }

    /**
     * 从内存缓存获取
     *
     * @param key
     * @return
     */
    private static Bitmap getBitmapFromMemCache(String key) {
        return sMemoryCache.get(key);
    }

    /**
     * 从磁盘/文件中加载图片
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    public static Bitmap loadBitmapFromDiskCache(
            String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "load bitmap from UI Thread,it is not recommended!");
        }
        if (sDiskLruCache == null) {
            return null;
        }
        Bitmap bitmap = null;
        String key = hashKeyFormUrl(url);
        DiskLruCache.Snapshot snapshot = sDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream =
                    (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = sImageResizer.decodeSampledBitmapFromFileDescriptor
                    (fileDescriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToMemoryCache(key, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 内存缓存的添加
     */
    private static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            sMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 通过网络请求获取图片
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    public static Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight)
            throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        if (sDiskLruCache == null) {
            return null;
        }
        String key = hashKeyFormUrl(url);
        DiskLruCache.Editor editor = sDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url, outputStream)) {
                // 写入磁盘成功后 editor 提交
                editor.commit();
            } else {
                editor.abort();
            }
            // sDiskLruCache 的检测流是否关闭和计算缓存是否超出的方法
            sDiskLruCache.flush();
        }
        //还是用了磁盘缓存加载的方法 因此有压缩
        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    /**
     * 从网络下载图片，然后通过输入流写入文件
     *
     * @param urlString
     * @param outputStream
     * @return
     */
    private static boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            //获取图片url
            final URL url = new URL(urlString);
            //创建HttpURLConnection建立网络连接
            urlConnection = (HttpURLConnection) url.openConnection();
            //创建输入流，从网络获取图片文件流
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            //创建输出流：DiskLruCache 的 editor 与 url 结合产生的输出流
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
            // 从网络读取流，写入到磁盘
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭网络和流
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 下载图片
     *
     * @param urlString
     * @return
     */
    public static Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (final IOException e) {
            Log.e(TAG, "Error in downloadBitmap:" + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 按照内存>文件>网络的顺序来加载图片
     * 同步加载
     */
    public static Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if (bitmap != null) {
            return bitmap;
        }
        try {
            bitmap = loadBitmapFromDiskCache(url, reqWidth, reqHeight);
            if (bitmap != null) {
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(url, reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap == null && !false) {
            bitmap = downloadBitmapFromUrl(url);
        }
        return bitmap;
    }
}
