package instinctools.android.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.account.GitHubAccount;
import instinctools.android.constans.Constants;
import instinctools.android.database.providers.EventsProvider;
import instinctools.android.database.providers.NotificationsProvider;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.loaders.AsyncAuthenticatorLoader;
import instinctools.android.models.github.authorization.AuthenticatorResponse;
import instinctools.android.services.github.GithubService;
import instinctools.android.services.github.authorization.GithubServiceAuthorization;
import instinctools.android.storages.SettingsStorage;

public class AuthenticatorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<AuthenticatorResponse> {
    public static final String EXTRA_TOKEN_TYPE = "instinctools.android.EXTRA_TOKEN_TYPE";

    public static final String BUNDLE_AUTH_CODE = "AUTH_CODE";
    public static final String INTENT_AUTH_TYPE = "AUTH_TYPE";

    public static final int PERMISSION_GET_ACCOUNTS = 100;

    public static final int LOADER_AUTHENTICATOR_ID = 1;

    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null)
            return;

        deleteAccount();

        switch (intent.getIntExtra(INTENT_AUTH_TYPE, -1)) {
            case 0:
            case 1:
                App.launchUrl(this, GithubServiceAuthorization.getAuthUrl(Constants.AUTH_CALLBACK_INITIAL));
                finish();
                return;
            default:
                break;
        }

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getDataString()))
            return;

        if (!intent.getDataString().contains(Constants.AUTH_CALLBACK_INITIAL))
            return;

        mProgressDialog = ProgressDialog.show(this, getString(R.string.msg_auth_title_dialog), getString(R.string.msg_auth_message_dialog), true);

        String code = intent.getDataString().substring(intent.getDataString().indexOf("code=") + 5);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_AUTH_CODE, code);
        getLoaderManager().initLoader(LOADER_AUTHENTICATOR_ID, bundle, this);
    }

    @Override
    public Loader<AuthenticatorResponse> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_AUTHENTICATOR_ID)
            return null;

        return new AsyncAuthenticatorLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<AuthenticatorResponse> loader, AuthenticatorResponse response) {
        final AccountManager am = AccountManager.get(this);
        final Account account = new GitHubAccount(response.getUser().getName());
        if (am.addAccountExplicitly(account, response.getUser().getEmail(), new Bundle()))
            am.setAuthToken(account, account.type, response.getAccessToken().getAcessToken());

        mProgressDialog.dismiss();

        GithubService.setAccessToken(response.getAccessToken().getAcessToken());

        // Automatic sync
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putInt(Constants.REPOSITORY_SYNC_TYPE, Constants.REPOSITORY_TYPE_MY);
        bundle.putInt(Constants.NOTIFICATION_SYNC_TYPE, Constants.NOTIFICATION_TYPE_ALL_TYPES);

        ContentResolver.requestSync(null, EventsProvider.AUTHORITY, bundle);
        ContentResolver.requestSync(null, NotificationsProvider.AUTHORITY, bundle);
        ContentResolver.requestSync(null, RepositoriesProvider.AUTHORITY, bundle);

        ContentResolver.setSyncAutomatically(account, EventsProvider.AUTHORITY, true);
        ContentResolver.setSyncAutomatically(account, NotificationsProvider.AUTHORITY, true);
        ContentResolver.setSyncAutomatically(account, RepositoriesProvider.AUTHORITY, true);

        ContentResolver.addPeriodicSync(account, EventsProvider.AUTHORITY, Bundle.EMPTY, SettingsStorage.getIntervalUpdateEvents() * 60);
        ContentResolver.addPeriodicSync(account, NotificationsProvider.AUTHORITY, Bundle.EMPTY, SettingsStorage.getIntervalUpdateNotifications() * 60);
        ContentResolver.addPeriodicSync(account, RepositoriesProvider.AUTHORITY, Bundle.EMPTY, SettingsStorage.getIntervalUpdateRepositories() * 60);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        finish();
    }

    @Override
    public void onLoaderReset(Loader<AuthenticatorResponse> loader) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_GET_ACCOUNTS && grantResults.length == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Snackbar.make(findViewById(R.id.content_main), R.string.msg_fail_grant_account_permissions, Snackbar.LENGTH_SHORT).show();
            else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
                deleteAccount();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private void deleteAccount() {
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.GET_ACCOUNTS }, PERMISSION_GET_ACCOUNTS);
        else {
            for (Account account : accountManager.getAccountsByType(GitHubAccount.TYPE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
                    accountManager.removeAccountExplicitly(account);
                else
                    accountManager.removeAccount(account, this, new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                        }
                    }, null);
            }
        }
    }
}
