package instinctools.android.storages;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import instinctools.android.R;
import instinctools.android.constans.Constants;

public class SettingsStorage {
    private static SharedPreferences mSettings = null;
    private static SharedPreferences.Editor mEditor = null;
    private static Context mContext = null;

    public static void init(Context context) {
        mContext = context;
    }

    private static void init() {
        if (mContext == null)
            throw new IllegalArgumentException("Before usage SettingsStorage: SettingsStorage.init(context)");

        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSettings.edit();
    }

    public static void clearAll() {
        mEditor.clear().apply();
    }

    public static int getIntervalUpdateRepositories() {
        if (mSettings == null)
            init();

        return Integer.parseInt(mSettings.getString(mContext.getString(R.string.title_pref_key_sync_repositories), String.valueOf(Constants.INTERVAL_UPDATE_REPO_SERVICES)));
    }

    public static int getIntervalUpdateNotifications() {
        if (mSettings == null)
            init();

        return Integer.parseInt(mSettings.getString(mContext.getString(R.string.title_pref_key_sync_notifications), String.valueOf(Constants.INTERVAL_UPDATE_NOTIFICATIONS)));
    }
    public static int getMaxSearchResult() {
        if (mSettings == null)
            init();

        return Integer.parseInt(mSettings.getString(mContext.getString(R.string.title_pref_key_general_max_search_result), String.valueOf(Constants.MAX_SEARCH_RESULT_BY_PAGE)));
    }

    public static boolean isNotificationSound() {
        if (mSettings == null)
            init();

        return mSettings.getBoolean(mContext.getString(R.string.title_pref_key_general_notification_sound), true);
    }
}