package instinctools.android;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;

import instinctools.android.account.GitHubAccount;
import instinctools.android.activity.AuthenticatorActivity;
import instinctools.android.activity.NotificationActivity;
import instinctools.android.constans.Constants;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.services.github.GithubService;
import instinctools.android.storages.ApplicationPersistantStorage;
import instinctools.android.storages.SettingsStorage;

public class App extends Application implements Application.ActivityLifecycleCallbacks {
    private static Context mContext;

    private static boolean isNotificationActivityVisible;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        GithubService.init(mContext, Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.SCOPES, Constants.CALLBACK_URL);
        ApplicationPersistantStorage.init(mContext);
        SettingsStorage.init(mContext);

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_sync_data, false);

        registerActivityLifecycleCallbacks(this);
    }

    public static void launchUrl(final Context context, final String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static boolean isNotificationActivityVisible() {
        return isNotificationActivityVisible;
    }

    public static Account getApplicationAccount() {
        final AccountManager accountManager = (android.accounts.AccountManager) mContext.getSystemService(Context.ACCOUNT_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED)
            return null;

        if (accountManager.getAccountsByType(GitHubAccount.TYPE).length == 0) {
            Intent intent = new Intent(mContext, AuthenticatorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(AuthenticatorActivity.INTENT_AUTH_TYPE, 1);
            mContext.startActivity(intent);
            return null;
        }

        return accountManager.getAccountsByType(GitHubAccount.TYPE)[0];
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof NotificationActivity)
            isNotificationActivityVisible = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (activity instanceof NotificationActivity)
            isNotificationActivityVisible = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
