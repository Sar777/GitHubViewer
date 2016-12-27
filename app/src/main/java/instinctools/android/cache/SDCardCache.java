package instinctools.android.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import instinctools.android.utility.Common;

/**
 * Created by orion on 23.12.16.
 */

public class SDCardCache extends BitmapCache<File> {
    private static final String TAG = "SDCardCache";

    private static final String CACHE_FILE_TYPE = ".bitmap";

    private boolean mDiskCacheBusy;

    private Context mContext;
    private final File mCacheFolder;

    private FilenameFilter mCacheFilter;

    public SDCardCache(long maxSize, Context context) {
        super(maxSize);

        this.mContext = context;
        this.mCacheFolder = Common.getDiskCacheDir(context);

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadingCache();
                cleanup();
            }
        }).start();
    }

    private void loadingCache() {
        Log.d(TAG, "Loading cache from SD Card");

        synchronized (mCacheLock) {
            mCacheSize = 0;
            mCacheStore.clear();

            // Cache pair: name - path
            for (File file : mCacheFolder.listFiles(mCacheFilter)) {
                mCacheSize += file.length();
                mCacheStore.put(file.getName().substring(0, file.getName().indexOf(CACHE_FILE_TYPE)), file);
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

            if (mCacheStore.containsKey(key))
                return false;

            if (mCacheSize > mMaxCacheSize)
                asyncCleanup();

            mCacheStore.put(key, new File(mCacheFolder.getAbsolutePath() + File.separator + key + CACHE_FILE_TYPE));
            asyncWriteToDisk(key, data);
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

            FutureTask<Bitmap> task = new FutureTask<>(new BitmapDecodeTask(file.getAbsolutePath()));
            new Thread(task).start();

            while (!task.isDone()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Log.d(TAG, "Get bitmap from SD card cache by: " + key);
                return task.get();
            } catch (InterruptedException | ExecutionException e) {
                Log.d(TAG, "Task interrupted or execution exception in getFromCache: " + key, e);
            }

            return null;
        }
    }

    @Override
    public void clear() {
        Log.d(TAG, "Clear bitmap cache storage");

        synchronized (mCacheLock) {
            for (Map.Entry<String, File> cache : mCacheStore.entrySet()) {
                cache.getValue().delete();
            }

            super.clear();
        }
    }

    private void cleanup() {
        if (mCacheStore.isEmpty())
            return;

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

    @Override
    public void asyncCleanup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cleanup();
            }
        }).start();
    }

    private void asyncWriteToDisk(final String key, final Bitmap data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream out = null;
                try {
                    File file = new File(mCacheFolder, key + CACHE_FILE_TYPE);
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
        }).start();
    }

    private class BitmapDecodeTask implements Callable<Bitmap> {
        private String mPath;

        public BitmapDecodeTask(String path) {
            this.mPath = path;
        }

        @Override
        public Bitmap call() throws Exception {
            return BitmapFactory.decodeFile(mPath);
        }
    }
}
