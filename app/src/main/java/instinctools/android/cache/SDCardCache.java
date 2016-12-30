package instinctools.android.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import instinctools.android.utility.Permission;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by orion on 23.12.16.
 */

public class SDCardCache extends BitmapCache<File> {
    private static final String TAG = "SDCardCache";

    private static final String CACHE_FILE_TYPE = ".bitmap";

    private boolean mDiskCacheBusy;

    private final Context mContext;

    private FilenameFilter mCacheFilter;

    public SDCardCache(long maxSize, Context context) {
        super(maxSize);

        this.mContext = context;

        mCacheFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(CACHE_FILE_TYPE))
                    return true;

                return false;
            }
        };

        this.mDiskCacheBusy = true;

        loadingCache();
        cleanup();
    }

    private void loadingCache() {
        Log.d(TAG, "Loading cache from SD Card");

        synchronized (mCacheLock) {
            mCacheSize = 0;
            mCacheStore.clear();

            File cacheFolder = getDiskCacheDir();
            if (cacheFolder != null) {
                // Cache pair: name - path
                for (File file : cacheFolder.listFiles(mCacheFilter)) {
                    mCacheSize += file.length();
                    mCacheStore.put(file.getName().substring(0, file.getName().indexOf(CACHE_FILE_TYPE)), file);
                }
            }

            mDiskCacheBusy = false;
            mCacheLock.notifyAll();
        }

        Log.d(TAG, "Loaded cache from SD Card: Bitmaps in cache: " + mCacheStore.size() + " Size: " + mCacheSize);
    }

    @Override
    public boolean addToCache(@NonNull String key, @NonNull Bitmap data) {
        synchronized (mCacheLock) {
            while (mDiskCacheBusy) {
                try {
                    mCacheLock.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Cache interrupt exception in addToCache", e);
                }
            }

            File cacheFolder = getDiskCacheDir();
            if (cacheFolder == null)
                return false;

            if (mCacheStore.containsKey(key))
                return false;

            if (mCacheSize > mMaxCacheSize)
                cleanup();

            mCacheStore.put(key, new File(cacheFolder.getAbsolutePath() + File.separator + key + CACHE_FILE_TYPE));
            writeToDisk(key, data);
            Log.d(TAG, "Added bitmap to SD card cache: Key: " + key + " Bitmap size: " + data.getByteCount());
        }

        return true;
    }

    @Override
    public Bitmap getFromCache(@NonNull String key) {
        synchronized (mCacheLock) {
            while (mDiskCacheBusy) {
                try {
                    mCacheLock.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Cache interrupt exception in getFromCache", e);
                }
            }

            final File file = mCacheStore.get(key);
            if (file == null) {
                Log.d(TAG, "Bitmap not found in SD card cache by key: " + key);
                return null;
            }

            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
    }

    @Override
    public void clear() {
        Log.d(TAG, "Clear bitmap cache storage");

        synchronized (mCacheLock) {
            for (Map.Entry<String, File> cache : mCacheStore.entrySet())
                cache.getValue().delete();

            super.clear();
        }
    }

    @Override
    public void cleanup() {
        synchronized (mCacheLock) {
            while (mDiskCacheBusy) {
                try {
                    mCacheLock.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Cache interrupt exception in getFromCache", e);
                }
            }

            mDiskCacheBusy = true;

            Log.d(TAG, "Resize bitmap cache storage. Current: Count: " + mCacheStore.size() + ", Size: " + mCacheSize + ", Max: " + mMaxCacheSize);

            List<File> files = new ArrayList<>(mCacheStore.values());
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File right, File left) {
                    return right.lastModified() < left.lastModified() ? -1 : right.lastModified() == left.lastModified() ? 0 : 1;
                }
            });

            Iterator<File> itr = files.iterator();
            while (mCacheSize >= mMaxCacheSize / 2 && itr.hasNext()) {
                File file = itr.next();
                mCacheSize -= file.length();
                file.delete();
                itr.remove();
            }

            mDiskCacheBusy = false;
            mCacheLock.notifyAll();

            loadingCache();
            Log.d(TAG, "Resize bitmap cache storage. Now: Count: " + mCacheStore.size() + ", Size: " + mCacheSize + ", Max: " + mMaxCacheSize);
        }
    }

    private void writeToDisk(final String key, final Bitmap data) {
        File cacheFolder = getDiskCacheDir();
        if (cacheFolder == null)
            return;

        FileOutputStream out = null;
        try {
            File file = new File(cacheFolder, key + CACHE_FILE_TYPE);
            out = new FileOutputStream(file);
            data.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            Log.e(TAG, "Fail compress bitmap in writeToDisk", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Fail close output stream in writeToDisk", e);
            }
        }
    }

    private File getDiskCacheDir() {
        if (Permission.hasReadWriteExternal(mContext)) {
            final String cachePath =
                    Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                            !isExternalStorageRemovable() ? mContext.getExternalCacheDir().getPath() :
                            mContext.getCacheDir().getPath();

            return new File(cachePath);
        }

        return null;
    }
}
