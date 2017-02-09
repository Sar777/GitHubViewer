package instinctools.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;

import instinctools.android.constans.Constants;
import instinctools.android.services.HttpRunAllService;
import instinctools.android.services.github.GithubService;
import instinctools.android.storages.ApplicationPersistantStorage;
import instinctools.android.storages.SettingsStorage;

public class App extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        GithubService.init(mContext, Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.SCOPES, Constants.CALLBACK_URL);
        ApplicationPersistantStorage.init(mContext);
        SettingsStorage.init(mContext);

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_sync_data, false);

        startService(new Intent(this, HttpRunAllService.class));
    }

    public static void launchUrl(final Context context, final String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    public static Context getAppContext() {
        return mContext;
    }
}
