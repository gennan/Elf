package com.example.elf.utils.imageloader.loader;

import android.graphics.Bitmap;

import com.example.elf.utils.imageloader.loader.strategy.cache.CacheManager;
import com.example.elf.utils.imageloader.loader.strategy.cache.DiskCacheStrategy;
import com.example.elf.utils.imageloader.loader.strategy.cache.MemCacheStrategy;
import com.example.elf.utils.imageloader.loader.strategy.cache.NetCacheStrategy;
import com.example.elf.utils.imageloader.loader.strategy.cache.ThreeCacheStrategy;

import static com.example.elf.utils.imageloader.loader.ConcreteLoadMethod.downloadBitmapFromUrl;


public class CoreLoad {
    private CacheManager load = new CacheManager();
    LoaderOptions mRequest;
    Bitmap mBitmap;
    private boolean mIsDiskLruCacheCreated = false;

    public CoreLoad(LoaderOptions request) {
        mRequest = request;
        switch (request.getWhereToLoadFrom()) {
            case "MemCache":
                load.setStrategy(new MemCacheStrategy());
                break;
            case "DiskCache":
                load.setStrategy(new DiskCacheStrategy());
                break;
            case "NetCache":
                load.setStrategy(new NetCacheStrategy());
                break;
            case "ThreeCache":
                load.setStrategy(new ThreeCacheStrategy());
            default:
                load.setStrategy(new ThreeCacheStrategy());
        }
    }

    public Bitmap load() {
        try {
            mBitmap = load.load(mRequest.getUri(), mRequest.getReqWidth(), mRequest.getReqHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mBitmap == null && !mIsDiskLruCacheCreated) {
            mBitmap = downloadBitmapFromUrl(mRequest.getUri());
        }
        return mBitmap;
    }
}
