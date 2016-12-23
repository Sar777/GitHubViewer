package instinctools.android;

import android.app.Application;
import android.content.Context;

import instinctools.android.cache.BitmapCacheMgr;
import instinctools.android.constans.Constants;

/**
 * Created by orion on 23.12.16.
 */
public class App extends Application {

    private static Context mContext;
    private static BitmapCacheMgr mBitmapCache;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        mBitmapCache = new BitmapCacheMgr.Builder(this).enableSDCardCache(Constants.DISK_MAX_CACHE_SIZE).build();
    }

    public static Context getContext() {
        return mContext;
    }

    public static BitmapCacheMgr getBitmapCache() {
        return mBitmapCache;
    }
}
