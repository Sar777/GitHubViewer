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

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by orion on 23.12.16.
 */

public class SDCardCache extends BitmapCache<File> {
    private static final String TAG = "SDCardCache";

    private static final String CACHE_FILE_TYPE = ".bitmap";

    private Context mContext;
    private final File mCacheFolder;

    private FilenameFilter mCacheFilter;

    public SDCardCache(long maxSize, Context context) {
        super(maxSize);

        this.mContext = context;
        this.mCacheFolder = getDiskCacheDir(context);

        mCacheFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(CACHE_FILE_TYPE))
                    return true;

                return false;
            }
        };

        loadingCache();

        resize();
    }

    private void loadingCache() {
        Log.d(TAG, "Loading cache from SD Card");

        synchronized (mCacheStore) {
            mCacheSize = 0;
            mCacheStore.clear();

            // Cache pair: name - path
            for (File file : mCacheFolder.listFiles(mCacheFilter)) {
                mCacheSize += file.length();
                mCacheStore.put(file.getName().substring(0, file.getName().indexOf(CACHE_FILE_TYPE)), file);
            }
        }

        Log.d(TAG, "Loaded cache from SD Card: Bitmaps in cache: " + mCacheStore.size() + " Size: " + mCacheSize);
    }

    @Override
    public boolean addToCache(@NonNull String key, @NonNull Bitmap data) {
        if (mCacheStore.containsKey(key))
            return false;

        if (mCacheSize > mMaxCacheSize)
            resize();

        if (!writeToDisk(key, data))
            return false;

        Log.d(TAG, "Added bitmap to SD card cache: Key: " + key + " Bitmap size: " + data.getByteCount());

        synchronized (mCacheStore) {
            mCacheStore.put(key, new File(mCacheFolder.getAbsolutePath() + File.separator + key + CACHE_FILE_TYPE));
        }

        return true;
    }

    @Override
    public Bitmap getFromCache(@NonNull String key) {
        String path;
        synchronized (mCacheStore) {
            File file = mCacheStore.get(key);

            if (file == null) {
                Log.d(TAG, "Bitmap not found in SD card cache by key: " + key);
                return null;
            }

            path = file.getAbsolutePath();
        }

        Log.d(TAG, "Get bitmap from SD card cache by: " + key);
        return BitmapFactory.decodeFile(path);
    }

    @Override
    public void clear() {
        Log.d(TAG, "Clear bitmap cache storage");

        synchronized (mCacheStore) {
            for (Map.Entry<String, File> cache : mCacheStore.entrySet()) {
                cache.getValue().delete();
            }

            super.clear();
        }
    }

    @Override
    public void resize() {
        if (mCacheStore.isEmpty())
            return;

        Log.d(TAG, "Resize bitmap cache storage. Current: Count: " + mCacheStore.size() + ", Size: " + mCacheSize + ", Max: " + mMaxCacheSize);

        synchronized (mCacheStore) {
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
        }

        Log.d(TAG, "Resize bitmap cache storage. Now: Count: " + mCacheStore.size() + ", Size: " + mCacheSize + ", Max: " + mMaxCacheSize);

        loadingCache();
    }

    public static File getDiskCacheDir(Context context) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath);
    }

    private boolean writeToDisk(String key, Bitmap data) {
        if (mCacheSize > mMaxCacheSize)
            return false;

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

        return true;
    }
}
