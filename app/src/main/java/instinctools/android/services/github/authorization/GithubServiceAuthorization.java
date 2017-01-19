package instinctools.android.services.github.authorization;

import android.support.annotation.NonNull;

import java.net.HttpURLConnection;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.authorization.AccessToken;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.GithubServices;
import instinctools.android.utility.Base64Hash;

public class GithubServiceAuthorization extends GithubServices {
    private static String getResetGrantUrl() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        return String.format("%s/%s/%s/%s", API_APPLICATION_URL, mClientId, FIELD_GRANTS, getAccessToken());
    }

    private static String getTokenUrl(@NonNull String code) {
        return TOKEN_URL +
                FIELD_CLIENT_ID + "=" + mClientId +
                "&" +
                FIELD_CLIENT_SECRET + "=" + mClientSecret +
                "&" +
                FIELD_CODE + "=" + code;
    }

    public static String getAuthUrl(String uriCallback) {
        return AUTH_URL +
                FIELD_CLIENT_ID + "=" + mClientId +
                "&" +
                FIELD_SCOPES + "=" + mScopes +
                "&" +
                FIELD_REDIRECT_URL + "=" + mBaseCallback + "/" + uriCallback;
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