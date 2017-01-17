package instinctools.android.services.github;

import android.content.Context;
import android.support.annotation.NonNull;

import java.net.HttpURLConnection;
import java.util.List;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.authorization.AccessToken;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.user.User;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.storages.GitHubSessionStorage;
import instinctools.android.utility.Base64Hash;

public class GithubServices {
    // Constants
    private static final String API_BASE_URL = "https://api.github.com";

    private static final String AUTH_URL = "https://github.com/login/oauth/authorize?";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token?";

    private static final String API_REPO_URL = API_BASE_URL + "/user/repos";
    private static final String API_APPLICATION_URL = API_BASE_URL + "/applications";
    private static final String API_USER_URL = API_BASE_URL + "/user";

    private static final String FIELD_CLIENT_ID = "client_id";
    private static final String FIELD_CLIENT_SECRET = "client_secret";
    private static final String FIELD_SCOPES = "scope";
    private static final String FIELD_REDIRECT_URL = "redirect_uri";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_GRANTS = "grants";

    //
    private static GitHubSessionStorage mSessionStorage;
    private static String mClientId;
    private static String mClientSecret;
    private static String mBaseCallback;
    private static String mScopes;

    public static void init(Context context, String clientId, String clientSecret, String scopes, String callbackUrl) {
        mSessionStorage = new GitHubSessionStorage(context);

        mClientId = clientId;
        mClientSecret = clientSecret;
        mScopes = scopes;

        mBaseCallback = callbackUrl;
    }

    public static String getAuthUrl(String uriCallback) {
        return AUTH_URL +
                FIELD_CLIENT_ID + "=" + mClientId +
                "&" +
                FIELD_SCOPES + "=" + mScopes +
                "&" +
                FIELD_REDIRECT_URL + "=" + mBaseCallback + "/" + uriCallback;
    }

    public static String getTokenUrl(@NonNull String code) {
        return TOKEN_URL +
                FIELD_CLIENT_ID + "=" + mClientId +
                "&" +
                FIELD_CLIENT_SECRET + "=" + mClientSecret +
                "&" +
                FIELD_CODE + "=" + code;
    }

    public static String getResetGrantUrl() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        return String.format("%s/%s/%s/%s", API_APPLICATION_URL, mClientId, FIELD_GRANTS, getAccessToken());
    }

    private static String getFormatAccessToken() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        return "token " + mSessionStorage.getAccessToken();
    }

    public static List<Repository> getRepositoryList() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_REPO_URL).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken());

        client.send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), Repository[].class);
    }

    public static void continueAuthorization(final String code, final GithubServiceListener<AccessToken> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        HttpClientFactory.HttpClient httpClient = HttpClientFactory.create(getTokenUrl(code));
        httpClient.
                setMethod(HttpClientFactory.METHOD_POST).
                addHeader(HttpClientFactory.HEADER_ACCEPT, HttpClientFactory.HEADER_ACCEPT_TYPE_JSON).
                send(new OnHttpClientListener() {
                    @Override
                    public void onError(int errCode) {
                        listener.onError(errCode);
                    }

                    @Override
                    public void onSuccess(int code, String content) {
                        AccessToken token = JsonTransformer.transform(content, AccessToken.class);
                        if (token == null)
                            return;

                        mSessionStorage.saveAccessToken(token.getAcessToken());
                        listener.onSuccess(token);
                    }
                });
    }

    public static User getUser() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_URL).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), User.class);
    }

    public static void getUser(final GithubServiceListener<User> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_URL).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken());

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode) {
                listener.onError(errCode);
            }

            @Override
            public void onSuccess(int code, String content) {
                if (code != HttpURLConnection.HTTP_OK) {
                    listener.onError(code);
                    return;
                }

                User user = JsonTransformer.transform(content, User.class);
                listener.onSuccess(user);
            }
        });
    }

    public static String getAccessToken() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        return mSessionStorage.getAccessToken();
    }

    public static void logout(final GithubServiceListener<Boolean> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(getResetGrantUrl()).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, "Basic " + Base64Hash.create(mClientId + ":" + mClientSecret)).
                setMethod(HttpClientFactory.METHOD_DELETE);

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode) {
                listener.onError(errCode);
            }

            @Override
            public void onSuccess(int code, String content) {
                if (code != HttpURLConnection.HTTP_NO_CONTENT) {
                    listener.onSuccess(false);
                    return;
                }

                mSessionStorage.resetAccessToken();
                listener.onSuccess(true);
            }
        });
    }
}
