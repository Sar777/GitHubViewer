package instinctools.android.services.github.user;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.PageLinks;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.organizations.Organization;
import instinctools.android.models.github.repositories.RepositoriesListResponse;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.user.User;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.services.github.GithubService;
import instinctools.android.services.github.GithubServiceListener;


public class GithubServiceUser extends GithubService {
    private static final String API_USER_URL = API_BASE_URL + "/user";
    private static final String API_USER_REPOS_URL = API_BASE_URL + "/users/%s/repos";
    private static final String API_USERS_URL = API_BASE_URL + "/users/%s";
    private static final String API_CURRENT_USER_ORGS = API_BASE_URL + "/user/orgs";
    private static final String API_USER_ORGS = API_BASE_URL + "/users/%s/orgs";
    private static final String API_REPO_MY_LIST_URL = API_BASE_URL + "/user/repos?affiliation=owner";

    private static final String API_USER_WATCH_REPOSITORIES = API_BASE_URL + "/user/subscriptions";
    private static final String API_USER_STAR_REPOSITORIES = API_BASE_URL + "/user/starred";

    public static void getCurrentUser(final GithubServiceListener<User> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_URL).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken());

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode, String content) {
                listener.onError(errCode, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                if (code != HttpURLConnection.HTTP_OK) {
                    listener.onError(code, null);
                    return;
                }

                User user = JsonTransformer.transform(content, User.class);
                listener.onSuccess(user);
            }
        });
    }

    public static User getCurrentUser() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_URL).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), User.class);
    }

    public static User getCurrentUserByToken(String accessToken) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_URL).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, "token " + accessToken).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), User.class);
    }

    public static void getUser(String username, final GithubServiceListener<User> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_USERS_URL, username)).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken());

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int code, String content) {
                listener.onError(code, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                listener.onSuccess((User)JsonTransformer.transform(content, User.class));
            }
        });
    }

    public static User getUser(String username) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_USERS_URL, username)).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), User.class);
    }

    public static void getUser(int userId, final GithubServiceListener<User> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_URL + "/" + userId).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                setMethod(HttpClientFactory.METHOD_GET);

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int code, String content) {
                listener.onError(code, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                listener.onSuccess((User)JsonTransformer.transform(content, User.class));
            }
        });
    }

    public static List<Repository> getMyRepositoryList() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_REPO_MY_LIST_URL).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), Repository[].class);
    }

    public static List<Repository> getWatchRepositoryList() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

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
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_USER_STAR_REPOSITORIES).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), Repository[].class);
    }

    public static void starredRepository(String fullName, boolean star, final GithubServiceListener<Boolean> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_USER_STAR_REPOSITORIES + "/%s", fullName)).
                setMethod(star ? HttpClientFactory.METHOD_PUT : HttpClientFactory.METHOD_DELETE).
                addHeader(HttpClientFactory.HEADER_CONTENT_LENGHT, String.valueOf(0)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken());

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode, String content) {
                listener.onError(errCode, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                listener.onSuccess(code == HttpURLConnection.HTTP_NO_CONTENT);
            }
        });
    }

    public static void isStarredRepository(String fullName, final GithubServiceListener<Boolean> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_USER_STAR_REPOSITORIES + "/%s", fullName)).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_CONTENT_LENGHT, String.valueOf(0)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken());

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode, String content) {
                listener.onError(errCode, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                listener.onSuccess(code == HttpURLConnection.HTTP_NO_CONTENT);
            }
        });
    }

    public static void watchedRepository(String fullName, boolean watch, final GithubServiceListener<Boolean> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_USER_WATCH_REPOSITORIES + "/%s", fullName)).
                setMethod(watch ? HttpClientFactory.METHOD_PUT : HttpClientFactory.METHOD_DELETE).
                addHeader(HttpClientFactory.HEADER_CONTENT_LENGHT, String.valueOf(0)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken());

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode, String content) {
                listener.onError(errCode, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                listener.onSuccess(code == HttpURLConnection.HTTP_NO_CONTENT);
            }
        });
    }

    public static void isWatchedRepository(String fullName, final GithubServiceListener<Boolean> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_USER_WATCH_REPOSITORIES + "/%s", fullName)).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_CONTENT_LENGHT, String.valueOf(0)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken());

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode, String content) {
                listener.onError(errCode, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                listener.onSuccess(code == HttpURLConnection.HTTP_NO_CONTENT);
            }
        });
    }

    public static List<Organization> getOrganizationsList(String username) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(username != null ? String.format(API_USER_ORGS, username) : API_CURRENT_USER_ORGS).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return new ArrayList<>();

        return JsonTransformer.transform(client.getContent(), Organization[].class);
    }


    public static RepositoriesListResponse getRepositoriesListResponse(String username) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_USER_REPOS_URL, username)).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        RepositoriesListResponse response = new RepositoriesListResponse((List<Repository>)JsonTransformer.transform(client.getContent(), Repository[].class));
        response.setPageLinks(new PageLinks(client.getResponseHeader(HttpClientFactory.HEADER_LINK)));
        return response;
    }

    public static void getRepositorisListByUrl(String url, final GithubServiceListener<RepositoriesListResponse> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        final HttpClientFactory.HttpClient client = HttpClientFactory.
                create(url).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                setMethod(HttpClientFactory.METHOD_GET);

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int code, String content) {
                listener.onError(code, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                RepositoriesListResponse response = new RepositoriesListResponse((List<Repository>)JsonTransformer.transform(content, Repository[].class));
                response.setPageLinks(new PageLinks(client.getResponseHeader(HttpClientFactory.HEADER_LINK)));

                listener.onSuccess(response);
            }
        });
    }
}
