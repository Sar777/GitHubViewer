package instinctools.android.cache;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Iterator;

/**
 * Created by orion on 23.12.16.
 */

class MemoryCache extends BitmapCache<Bitmap> {
    private static final String TAG = "MemoryCache";

    MemoryCache(long maxSize) {
        super(maxSize);
    }

    @Override
    public boolean addToCache(@NonNull String key, @NonNull Bitmap data) {
        synchronized (mCacheLock) {
            if (mCacheStore.get(key) != null)
                return false;

            if (mCacheSize > mMaxCacheSize)
                cleanup();

            Log.d(TAG, "Add bitmap to memory cache by: " + key);
            mCacheStore.put(key, data);
        }

        return true;
    }

    @Override
    public Bitmap getFromCache(@NonNull String key) {
        synchronized (mCacheLock) {
            Bitmap bitmap = mCacheStore.get(key);
            if (bitmap == null)
                return null;

            Log.d(TAG, "Get bitmap from memory cache by: " + key);
            return bitmap;
        }
    }

    @Override
    public void cleanup() {
        synchronized (mCacheLock) {
            Log.d(TAG, "Resize bitmap cache storage. Current: Count: " + mCacheStore.size() + ", Size: " + mCacheSize + ", Max: " + mMaxCacheSize);
            Iterator<Bitmap> itr = mCacheStore.values().iterator();
            while (mCacheSize >= mMaxCacheSize / 2 && itr.hasNext()) {
                Bitmap bitmap = itr.next();
                mCacheSize -= bitmap.getByteCount();
                itr.remove();
            }

            Log.d(TAG, "Resize bitmap cache storage. Now: Count: " + mCacheStore.size() + ", Size: " + mCacheSize + ", Max: " + mMaxCacheSize);
        }
    }
}
