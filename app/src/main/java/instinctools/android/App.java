package instinctools.android;

import android.app.Application;
import android.content.Context;

import instinctools.android.database.DBConstants;
import instinctools.android.storages.PersistantStorage;

public class App extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        PersistantStorage.init(mContext);

        // TODO REMOVE ME
        deleteDatabase(DBConstants.DB_NAME);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
