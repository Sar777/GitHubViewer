package instinctools.android.services.github;

import android.content.Context;

import instinctools.android.storages.GitHubSessionStorage;

public class GithubServices {
    // Constants
    protected static final String API_BASE_URL = "https://api.github.com";

    protected static final String AUTH_URL = "https://github.com/login/oauth/authorize?";
    protected static final String TOKEN_URL = "https://github.com/login/oauth/access_token?";

    protected static final String API_APPLICATION_URL = API_BASE_URL + "/applications";

    protected static final String FIELD_CLIENT_ID = "client_id";
    protected static final String FIELD_CLIENT_SECRET = "client_secret";
    protected static final String FIELD_SCOPES = "scope";
    protected static final String FIELD_REDIRECT_URL = "redirect_uri";
    protected static final String FIELD_CODE = "code";
    protected static final String FIELD_GRANTS = "grants";

    //
    protected static GitHubSessionStorage mSessionStorage;
    protected static String mClientId;
    protected static String mClientSecret;
    protected static String mBaseCallback;
    protected static String mScopes;

    public static void init(Context context, String clientId, String clientSecret, String scopes, String callbackUrl) {
        mSessionStorage = new GitHubSessionStorage(context);

        mClientId = clientId;
        mClientSecret = clientSecret;
        mScopes = scopes;

        mBaseCallback = callbackUrl;
    }

    protected static String getFormatAccessToken() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        return "token " + mSessionStorage.getAccessToken();
    }

    public static String getAccessToken() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        return mSessionStorage.getAccessToken();
    }
}
