package instinctools.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import instinctools.android.services.HttpUpdateDataService;

/**
 * Created by orion on 29.12.16.
 */

public class App extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        Intent intentService = new Intent(this, HttpUpdateDataService.class);
        startService(intentService);
    }

    public static Context getAppContext() {
        return mContext;
    }
}