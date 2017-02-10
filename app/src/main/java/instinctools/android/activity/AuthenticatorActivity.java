package instinctools.android.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.account.GitHubAccount;
import instinctools.android.constans.Constants;
import instinctools.android.loaders.AsyncAuthenticatorLoader;
import instinctools.android.models.github.authorization.AuthenticatorResponse;
import instinctools.android.services.github.authorization.GithubServiceAuthorization;

public class AuthenticatorActivity extends AccountAuthenticatorActivity implements LoaderManager.LoaderCallbacks<AuthenticatorResponse> {
    public static final String EXTRA_TOKEN_TYPE = "instinctools.android.EXTRA_TOKEN_TYPE";

    public static final String BUNDLE_AUTH_CODE = "AUTH_CODE";

    public static final int LOADER_AUTHENTICATOR_ID = 1;

    // View
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onNewIntent(getIntent());

        App.launchUrl(this, GithubServiceAuthorization.getAuthUrl(Constants.AUTH_CALLBACK_INITIAL));
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
        final Bundle result = new Bundle();
        final Account account = new GitHubAccount(response.getUser().getName());
        if (am.addAccountExplicitly(account, response.getUser().getEmail(), new Bundle())) {
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, response.getAccessToken().getAcessToken());
            am.setAuthToken(account, account.type, response.getAccessToken().getAcessToken());
        } else {
            //result.putString(AccountManager.KEY_ERROR_MESSAGE, getString(R.string.account_already_exists));
        }
        setAccountAuthenticatorResult(result);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onLoaderReset(Loader<AuthenticatorResponse> loader) {

    }
}
