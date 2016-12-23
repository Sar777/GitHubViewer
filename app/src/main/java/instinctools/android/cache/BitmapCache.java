package instinctools.android.cache;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by orion on 23.12.16.
 */

public abstract class BitmapCache<A> {
    protected long mCacheSize;
    protected final long mMaxCacheSize;

    protected Map<String, A> mCacheStore = new HashMap<>();

    public BitmapCache(long maxSize) {
        if (maxSize <= 0)
            throw new IllegalArgumentException("Cache constructor: bad max size cache value: " + maxSize);

        this.mMaxCacheSize = maxSize;
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

    abstract void resize();

    abstract boolean addToCache(String key, Bitmap bitmap);
    abstract Bitmap getFromCache(String key);
}
