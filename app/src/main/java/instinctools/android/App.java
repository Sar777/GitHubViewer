package instinctools.android;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;

import instinctools.android.constans.Constants;
import instinctools.android.services.github.GithubServices;
import instinctools.android.storages.ApplicationPersistantStorage;

public class App extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        GithubServices.init(mContext, Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.SCOPES, Constants.CALLBACK_URL);
        ApplicationPersistantStorage.init(mContext);
    }

    public static void launchUrl(final Context context, final String url) {
        final CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient client) {
                final CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                final CustomTabsIntent intent = builder.build();
                client.warmup(0L); // This prevents backgrounding after redirection
                intent.launchUrl(context, Uri.parse(url));
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {}
        };

        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", connection);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
