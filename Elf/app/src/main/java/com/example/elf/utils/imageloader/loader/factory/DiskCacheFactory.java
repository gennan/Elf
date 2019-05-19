package com.example.elf.utils.imageloader.loader.factory;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;


import com.example.elf.utils.imageloader.diskLruCache.DiskLruCache;

import java.io.File;
import java.io.IOException;



public class DiskCacheFactory {

    //设置当磁盘空间大于多少时才可进行磁盘缓存
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//50MB

    private DiskCacheFactory() {
    }


    public static DiskLruCache createDiskCache(Context context) {
        File diskCacheDir = getDiskCacheDir(context, "bitmap");
        // 如果文件不存在则需要手动创建
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }


        // 当磁盘空间大于所需空间的时候，才能进行磁盘缓存

        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {

            try {
                return DiskLruCache.open(

                        diskCacheDir, 1, 1, DISK_CACHE_SIZE);


            } catch (IOException e) {

                e.printStackTrace();

            }
        }
        return null;
    }

    /**
     * 该方法用于获取缓存地址
     *
     * @param context
     * @param uniqueName
     * @return
     */

    public static File getDiskCacheDir(Context context, String uniqueName) {
        boolean externalStorageAvailable =
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取文件可用空间大小
     *
     * @param path
     * @return
     */

    private static long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }
}
