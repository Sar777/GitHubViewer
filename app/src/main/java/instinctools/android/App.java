package instinctools.android;

import android.app.Application;
import android.content.Context;

import instinctools.android.database.DBConstants;

/**
 * Created by orion on 29.12.16.
 */

public class App extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        /// TODO: REMOVE ME
        deleteDatabase(DBConstants.DB_NAME);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
