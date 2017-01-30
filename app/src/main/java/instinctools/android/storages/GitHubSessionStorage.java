package instinctools.android.storages;

import android.content.Context;
import android.content.SharedPreferences;

public class GitHubSessionStorage {
    private static final String STORAGE_NAME = "Github_Preferences";
    private static final String API_ACCESS_TOKEN = "access_token";

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

    public String getAccessToken() {
        return getStringProperty(API_ACCESS_TOKEN);
    }

    public void saveAccessToken(String token) {
        addProperty(API_ACCESS_TOKEN, token);
    }

    public void resetAccessToken() {
        removeProperty(API_ACCESS_TOKEN);
    }
}