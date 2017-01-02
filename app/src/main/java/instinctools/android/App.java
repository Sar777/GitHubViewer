package instinctools.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by orion on 29.12.16.
 */

public class App extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
}
