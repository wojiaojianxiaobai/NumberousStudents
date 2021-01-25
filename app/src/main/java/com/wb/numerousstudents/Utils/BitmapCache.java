package com.wb.numerousstudents.Utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class BitmapCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> mCache;

    public BitmapCache() {
        int maxSize = 4 * 1024 * 1024;
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        Log.i("leslie", "get cache " + url);
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        Log.i("leslie", "add cache " + url);
        if (bitmap != null) {
            mCache.put(url, bitmap);
        }
    }
}
