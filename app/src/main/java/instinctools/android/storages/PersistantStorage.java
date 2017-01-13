package instinctools.android.storages;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by orion on 13.1.17.
 */

public class PersistantStorage {
    public static final String STORAGE_NAME = "GITHUB";

    private static SharedPreferences mSettings = null;
    private static SharedPreferences.Editor mEditor = null;
    private static Context context = null;

    public static void init(Context context) {
        PersistantStorage.context = context;
    }

    private static void init() {
        mSettings = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        mEditor.apply();
    }

    public static void addProperty(String name, String value) {
        if (mSettings == null)
            init();

        mEditor.putString( name, value );
        mEditor.apply();
    }

    public static String getProperty(String name) {
        if (mSettings == null)
            init();

        return mSettings.getString(name, null);
    }
}