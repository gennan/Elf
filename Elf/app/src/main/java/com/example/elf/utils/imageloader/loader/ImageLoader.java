package com.example.elf.utils.imageloader.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.example.elf.R;
import com.example.elf.utils.imageloader.diskLruCache.DiskLruCache;
import com.example.elf.utils.imageloader.loader.factory.DiskCacheFactory;
import com.example.elf.utils.imageloader.loader.factory.MemCacheFactory;
import com.example.elf.utils.imageloader.loader.factory.ThreadPoolFactory;

import static com.example.elf.utils.imageloader.loader.ConcreteLoadMethod.loadBitmapFromMemCache;


public class ImageLoader {

    // 设置一个TAG方便打Log用
    private static final String TAG = "ImageLoader";

    //为避免重复创建浪费资源因此选择使用单例模式
    private static volatile ImageLoader sImageLoader = null;

    //创建ImageLoader对象时传进来的context给下面我们定义的context
    private Context mContext;

    //下面异步加载图片时需要这里的mMainHandler将消息发送到消息队列中
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            imageView.setImageBitmap(result.bitmap);
            String uri = (String) imageView.getTag(IMAGELOADER_TAG_KEY_URI);
            if (uri.equals(result.uri)) {
                imageView.setImageBitmap(result.bitmap);
            } else {
                Log.w(TAG, "set image bitmap,but url has changed,ignored!");
            }
        }
    };

    //磁盘缓存需要创建一个相应的对象
    public static DiskLruCache sDiskLruCache;
    //内存缓存需要new一个map集合
    public static LruCache<String, Bitmap> sMemoryCache;
    //给ImageLoader设置一个tag
    public static final int IMAGELOADER_TAG_KEY_URI = R.id.imageloader_uri;
    //成功将消息发送到消息队列中的结果为1
    private static final int MESSAGE_POST_RESULT = 1;

    public static final int DISK_CACHE_INDEX = 0;
    public static final int IO_BUFFER_SIZE = 8 * 1024;
    CoreLoad mLoad;

    /**
     * 私有的构造方法 不让外部获取
     */

    private ImageLoader(Context context) {
        mContext = context;
        sMemoryCache = MemCacheFactory.createMemCache();
        sDiskLruCache = DiskCacheFactory.createDiskCache(context);
    }


    /**
     * 给外部暴露一个static的方法来获得实例ImageLoader
     */

    public static ImageLoader getImageLoaderInstance(Context context) {
        if (sImageLoader == null) {
            synchronized (ImageLoader.class) {
                if (sImageLoader == null) {
                    sImageLoader = new ImageLoader(context);
                }
            }
        }
        return sImageLoader;
    }


    /**
     * 异步加载图片 把加载图片的任务放入线程池中
     */
    public void bindBitmap(
            final LoaderOptions request) {
        request.getImageView().setTag(IMAGELOADER_TAG_KEY_URI, request.getUri());
        Bitmap bitmap = loadBitmapFromMemCache(request.getUri());
        if (bitmap != null) {
            request.getImageView().setImageBitmap(bitmap);
            return;
        }

        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                mLoad = new CoreLoad(request);
                Bitmap bitmap = mLoad.load();
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(request.getImageView(), request.getUri(), bitmap);
                    //发送到消息队列中
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();
                }
            }
        };
        ThreadPoolFactory.createThreadPool().execute(loadBitmapTask);
    }
}