package instinctools.android.storages;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.SharedPreferences.Editor;

public class ApplicationPersistantStorage {
    public static final String STORAGE_NAME = "Application_Preferences";

    private static SharedPreferences mSettings = null;
    private static Editor mEditor = null;
    private static Context mContext = null;

    public static void init(Context context) {
        ApplicationPersistantStorage.mContext = context;
    }

    private static void init() {
        mSettings = mContext.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    public static void addProperty(String name, String value) {
        if (mSettings == null)
            init();

        mEditor.putString(name, value);
        mEditor.apply();
    }

    public static void addProperty(String name, Boolean value) {
        if (mSettings == null)
            init();

        mEditor.putBoolean(name, value);
        mEditor.apply();
    }

    public static void addProperty(String name, int value) {
        if (mSettings == null)
            init();

        mEditor.putInt(name, value);
        mEditor.apply();
    }

    public static void addProperty(String name, float value) {
        if (mSettings == null)
            init();

        mEditor.putFloat(name, value);
        mEditor.apply();
    }

    public static String getStringProperty(String name) {
        if (mSettings == null)
            init();

        return mSettings.getString(name, null);
    }

    public static boolean getBoolProperty(String name) {
        if (mSettings == null)
            init();

        return mSettings.getBoolean(name, false);
    }

    public static int getIntProperty(String name) {
        if (mSettings == null)
            init();

        return mSettings.getInt(name, -1);
    }

    public static float getFloatProperty(String name) {
        if (mSettings == null)
            init();

        return mSettings.getFloat(name, 0.0f);
    }
}
