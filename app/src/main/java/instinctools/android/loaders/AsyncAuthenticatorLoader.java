package instinctools.android.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import instinctools.android.activity.AuthenticatorActivity;
import instinctools.android.models.github.authorization.AuthenticatorResponse;
import instinctools.android.services.github.authorization.GithubServiceAuthorization;
import instinctools.android.services.github.user.GithubServiceUser;

public class AsyncAuthenticatorLoader extends AsyncTaskLoader<AuthenticatorResponse> {
    private String mCode;

    public AsyncAuthenticatorLoader(Context context, Bundle bundle) {
        super(context);

        mCode = bundle.getString(AuthenticatorActivity.BUNDLE_AUTH_CODE);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public AuthenticatorResponse loadInBackground() {
        AuthenticatorResponse response = new AuthenticatorResponse(GithubServiceAuthorization.getAccessToken(mCode));
        response.setUser(GithubServiceUser.getCurrentUserByToken(response.getAccessToken().getAcessToken()));
        return response;
    }
}
