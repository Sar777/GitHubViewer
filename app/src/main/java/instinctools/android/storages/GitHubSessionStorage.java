package instinctools.android.storages;

import android.content.Context;
import android.content.SharedPreferences;

public class GitHubSessionStorage {
    private static final String STORAGE_NAME = "Github_Preferences";

    private SharedPreferences mSettings = null;
    private SharedPreferences.Editor mEditor = null;
    private Context mContext = null;

    public GitHubSessionStorage(Context context) {
        this.mContext = context;
    }

    private void init() {
        mSettings = mContext.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    private void removeProperty(String name) {
        if (mSettings == null)
            init();

        mEditor.remove(name);
        mEditor.apply();
    }

    private void addProperty(String name, String value) {
        if (mSettings == null)
            init();

        mEditor.putString(name, value);
        mEditor.apply();
    }

    private String getStringProperty(String name) {
        if (mSettings == null)
            init();

        return mSettings.getString(name, null);
    }
}