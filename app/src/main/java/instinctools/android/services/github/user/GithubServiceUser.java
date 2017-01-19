package instinctools.android.services.github.user;

import java.net.HttpURLConnection;
import java.util.List;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.user.User;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.GithubServices;

/**
 * Created by orion on 19.1.17.
 */

public class GithubServiceUser extends GithubServices {
    private static final String API_USER_URL = API_BASE_URL + "/user";
    private static final String API_USER_WATCH_REPOSITORIES = API_BASE_URL + "/user/subscriptions";
    private static final String API_USER_STAR_REPOSITORIES = API_BASE_URL + "/user/starred";

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

    public static List<Repository> getWatchRepositoryList() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_WATCH_REPOSITORIES).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), Repository[].class);
    }

    public static List<Repository> getStarRepositoryList() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_STAR_REPOSITORIES).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), Repository[].class);
    }
}
