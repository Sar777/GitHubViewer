package instinctools.android.services.github;

import java.net.HttpURLConnection;
import java.util.List;

import instinctools.android.constans.Constants;
import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.authorization.AuthToken;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.user.User;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.storages.PersistantStorage;
import instinctools.android.utility.Base64Hash;

public class GithubServices {
    private static final String API_BASE_URL = "https://api.github.com";

    private static final String API_REPO_URL = API_BASE_URL + "/user/repos";
    private static final String API_AUTH_URL = API_BASE_URL + "/authorizations";
    private static final String API_USER_URL = API_BASE_URL + "/user";

    public static void getRepositoryList(final GithubServiceListener<List<Repository>> listener) {
        HttpClientFactory.create(API_REPO_URL).
                setMethod("GET").
                addHeader("Authorization", "token " + PersistantStorage.getStringProperty(Constants.PROPERTY_AUTH_TOKEN)).
                send(new OnHttpClientListener() {
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

                        List<Repository> repositories = JsonTransformer.transform(content, Repository[].class);
                        listener.onSuccess(repositories);
                    }
                });
    }

    public static List<Repository> getRepositoryList() {
        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_REPO_URL).
                setMethod("GET").
                addHeader("Authorization", "token " + PersistantStorage.getStringProperty(Constants.PROPERTY_AUTH_TOKEN));

        client.send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), Repository[].class);
    }

    public static void authorization(final String email, final String password, final GithubServiceListener<AuthToken> listener) {
        final String authData = "{\"scopes\":[\"repo\"],\"note\":\"Test APP\"}";

        HttpClientFactory.HttpClient httpClient = HttpClientFactory.create(API_AUTH_URL);
        httpClient.
                setMethod("POST").
                addHeader("Authorization", "Basic " + Base64Hash.create(email + ":" + password)).
                setData(authData).
                send(new OnHttpClientListener() {
                    @Override
                    public void onError(int errCode) {
                        listener.onError(errCode);
                    }

                    @Override
                    public void onSuccess(int code, String content) {
                        if (code != HttpURLConnection.HTTP_CREATED) {
                            listener.onError(code);
                            return;
                        }

                        AuthToken token = JsonTransformer.transform(content, AuthToken.class);

                        if (token != null) {
                            PersistantStorage.addProperty("AUTH_BASIC", Base64Hash.create(email + ":" + password));
                            PersistantStorage.addProperty("AUTH_TOKEN", token.getToken());
                            PersistantStorage.addProperty("AUTH_TOKEN_ID", String.valueOf(token.getId()));
                        }

                        listener.onSuccess(token);
                    }
                });
    }

    public static AuthToken getAuthToken() {
        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_AUTH_URL + "/" + PersistantStorage.getStringProperty(Constants.PROPERTY_AUTH_TOKEN_ID)).
                setMethod("GET").
                addHeader("Authorization", "Basic " + PersistantStorage.getStringProperty(Constants.PROPERTY_AUTH_BASIC)).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), AuthToken.class);
    }

    public static User getUser() {
        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_URL).
                setMethod("GET").
                addHeader("Authorization", "token " + PersistantStorage.getStringProperty(Constants.PROPERTY_AUTH_TOKEN)).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), User.class);
    }

    public static void getUser(final GithubServiceListener<User> listener) {
        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_URL).
                setMethod("GET").
                addHeader("Authorization", "token " + PersistantStorage.getStringProperty(Constants.PROPERTY_AUTH_TOKEN));

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

    public static boolean logout() {
        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_AUTH_URL + "/" + PersistantStorage.getStringProperty(Constants.PROPERTY_AUTH_TOKEN_ID)).
                setMethod("DELETE").
                addHeader("Authorization", "token " + PersistantStorage.getStringProperty(Constants.PROPERTY_AUTH_TOKEN)).send();


        return client.getCode() == HttpURLConnection.HTTP_NO_CONTENT;
    }

    public static void logout(final GithubServiceListener<Boolean> listener) {
        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_AUTH_URL + "/" + PersistantStorage.getStringProperty(Constants.PROPERTY_AUTH_TOKEN_ID)).
                setMethod("DELETE").
                addHeader("Authorization", "Basic " + PersistantStorage.getStringProperty(Constants.PROPERTY_AUTH_BASIC));

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode) {
                listener.onError(errCode);
            }

            @Override
            public void onSuccess(int code, String content) {
                listener.onSuccess(code == HttpURLConnection.HTTP_NO_CONTENT);
            }
        });
    }
}
