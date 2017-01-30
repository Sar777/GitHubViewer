package instinctools.android.services.github;

import android.content.Context;

import instinctools.android.storages.GitHubSessionStorage;

public class GithubService {
    // Constants
    protected static final String API_BASE_URL = "https://api.github.com";

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
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        return "token " + mSessionStorage.getAccessToken();
    }

    public static String getAccessToken() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        return mSessionStorage.getAccessToken();
    }
}
