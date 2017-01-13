package instinctools.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import instinctools.android.database.DBConstants;
import instinctools.android.services.HttpUpdateDataService;
import instinctools.android.storages.PersistantStorage;

/**
 * Created by orion on 29.12.16.
 */

public class App extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        PersistantStorage.init(mContext);

        Intent intentService = new Intent(this, HttpUpdateDataService.class);
        startService(intentService);

        // TODO REMOVE ME
        deleteDatabase(DBConstants.DB_NAME);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
