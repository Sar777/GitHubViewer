package instinctools.android.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

import instinctools.android.utility.MD5Hash;

/**
 * Created by orion on 23.12.16.
 */
public class BitmapCacheMgr {
    private static final String TAG = "BitmapCacheMgr";

    private BitmapCache<File> mSDCardCache;
    private BitmapCache<Bitmap> mMemoryCache;

    private BitmapCacheMgr(Builder builder) {
        this.mMemoryCache = builder.mMemoryCache;
        this.mSDCardCache = builder.mSDCache;
    }

    public void addToCache(@NonNull String key, @NonNull Bitmap bitmap) {
        String keyHash = MD5Hash.create(key);

        Log.d(TAG, "Add bitmap to cache storage. Key " + keyHash + " Bitmap Size: " + bitmap.getByteCount());

        mMemoryCache.addToCache(keyHash, bitmap);

        if (mSDCardCache != null)
            mSDCardCache.addToCache(MD5Hash.create(key), bitmap);
    }

    public Bitmap getFromCache(@NonNull String key) {
        String keyHash = MD5Hash.create(key);

        Bitmap cacheBitmap = mMemoryCache.getFromCache(keyHash);
        if (cacheBitmap != null)
            return cacheBitmap;

        return mSDCardCache != null ? mSDCardCache.getFromCache(keyHash) : null;
    }

    public static class Builder {
        private BitmapCache<File> mSDCache;
        private BitmapCache<Bitmap> mMemoryCache;

        public Builder() {
            // Default 1/8 heap
            this.mMemoryCache = new MemoryCache((int)(Runtime.getRuntime().maxMemory() / 1024) / 8);
        }

        public Builder setMaxSizeMemoryCache(long maxSize) {
            mMemoryCache = new MemoryCache(maxSize);
            return this;
        }

        public Builder enableSDCardCache(long maxSize, Context context) {
            mSDCache = new SDCardCache(maxSize, context);
            return this;
        }

        public BitmapCacheMgr build() {
            return new BitmapCacheMgr(this);
        }
    }
}
