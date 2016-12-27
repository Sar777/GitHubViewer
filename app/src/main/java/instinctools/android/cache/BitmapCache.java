package instinctools.android.cache;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by orion on 23.12.16.
 */

public abstract class BitmapCache<A> {
    final Object mCacheLock = new Object();

    long mCacheSize;
    final long mMaxCacheSize;
    Map<String, A> mCacheStore;

    BitmapCache(long maxSize) {
        if (maxSize <= 0)
            throw new IllegalArgumentException("Cache constructor: bad max size cache value: " + maxSize);

        this.mMaxCacheSize = maxSize;
        this.mCacheStore = new HashMap<>();
    }

    long getSize() {
        return mCacheSize;
    }

    long getMaxCacheSize() {
        return mMaxCacheSize;
    }

    void clear() {
        mCacheStore.clear();
    }

    abstract void asyncCleanup();
    abstract boolean addToCache(String key, Bitmap bitmap);
    abstract Bitmap getFromCache(String key);
}
