package instinctools.android.services.github.repository;

import java.net.HttpURLConnection;
import java.util.List;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.PageLinks;
import instinctools.android.models.github.commits.Commit;
import instinctools.android.models.github.commits.CommitsListResponse;
import instinctools.android.models.github.contributors.ContributorsListResponse;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.issues.Issue;
import instinctools.android.models.github.issues.IssueState;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.repositories.RepositoryReadme;
import instinctools.android.models.github.user.UserContributor;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryReadmeTransformer;
import instinctools.android.services.github.Direction;
import instinctools.android.services.github.GithubService;
import instinctools.android.services.github.GithubServiceListener;

public class GithubServiceRepository extends GithubService {
    private static final String API_REPOSITORY_README_URL = API_BASE_URL + "/repos/%s/readme";
    private static final String API_REPOSITORY = API_BASE_URL + "/repos/%s";
    private static final String API_REPOSITORY_ISSUES = API_BASE_URL + "/repos/%s/issues";
    private static final String API_REPOSITORY_CONTRIBUTORS = API_BASE_URL + "/repos/%s/contributors";
    private static final String API_REPOSITORY_COMMITS = API_BASE_URL + "/repos/%s/commits";

    private static final String FIELD_STATE = "state";
    private static final String FIELD_DIRECTION = "direction";

    private static String getIssuesRequestFormat(IssueState state, Direction direction) {
        return "?" +
                FIELD_STATE +
                "=" +
                state.toString() +
                "&" +
                FIELD_DIRECTION +
                "=" +
                direction.toString();
    }

    public static RepositoryReadme getRepositoryReadme(String fullname) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_REPOSITORY_README_URL, fullname)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                setMethod(HttpClientFactory.METHOD_GET).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return (RepositoryReadme) JsonTransformer.transform(client.getContent(), RepositoryReadmeTransformer.class);
    }

    public static void getRepositoryReadme(String fullname, final GithubServiceListener<RepositoryReadme> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_REPOSITORY_README_URL, fullname)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                setMethod(HttpClientFactory.METHOD_GET);

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode, String content) {
                listener.onError(errCode, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                if (code != HttpURLConnection.HTTP_OK) {
                    listener.onSuccess(null);
                    return;
                }

                listener.onSuccess((RepositoryReadme) JsonTransformer.transform(content, RepositoryReadme.class));
            }
        });
    }

    public static void getRepository(String fullname, final GithubServiceListener<Repository> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_REPOSITORY, fullname)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                setMethod(HttpClientFactory.METHOD_GET);

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode, String content) {
                listener.onError(errCode, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                if (code != HttpURLConnection.HTTP_OK) {
                    listener.onSuccess(null);
                    return;
                }

                listener.onSuccess((Repository) JsonTransformer.transform(content, Repository.class));
            }
        });
    }

    public static Repository getRepository(String fullname) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_REPOSITORY, fullname)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                setMethod(HttpClientFactory.METHOD_GET).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return (Repository) JsonTransformer.transform(client.getContent(), Repository.class);
    }

    public static void getRepositoryIssues(String fullname, IssueState state, Direction direction, final GithubServiceListener<List<Issue>> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_REPOSITORY_ISSUES, fullname) + getIssuesRequestFormat(state, direction)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                setMethod(HttpClientFactory.METHOD_GET);

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode, String content) {
                listener.onError(errCode, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                if (code != HttpURLConnection.HTTP_OK) {
                    listener.onSuccess(null);
                    return;
                }

                listener.onSuccess((List<Issue>) JsonTransformer.transform(content, Issue[].class));
            }
        });
    }

    public static ContributorsListResponse getContributors(String fullname) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_REPOSITORY_CONTRIBUTORS, fullname)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                addHeader(HttpClientFactory.HEADER_ACCEPT, HttpClientFactory.HEADER_ACCEPT_TYPE_CUSTOM_GITHUB).
                setMethod(HttpClientFactory.METHOD_GET).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        ContributorsListResponse response = new ContributorsListResponse((List<UserContributor>)JsonTransformer.transform(client.getContent(), UserContributor[].class));
        response.setPageLinks(new PageLinks(client.getResponseHeader(HttpClientFactory.HEADER_LINK)));
        return response;
    }

    public static void getContributionsByUrl(String url, final GithubServiceListener<ContributorsListResponse> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        final HttpClientFactory.HttpClient client = HttpClientFactory.
                create(url).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                addHeader(HttpClientFactory.HEADER_ACCEPT, HttpClientFactory.HEADER_ACCEPT_TYPE_CUSTOM_GITHUB).
                setMethod(HttpClientFactory.METHOD_GET);

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int code, String content) {
                listener.onError(code, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                ContributorsListResponse response = new ContributorsListResponse((List<UserContributor>)JsonTransformer.transform(content, UserContributor[].class));
                response.setPageLinks(new PageLinks(client.getResponseHeader(HttpClientFactory.HEADER_LINK)));

                listener.onSuccess(response);
            }
        });
    }

    public static CommitsListResponse getCommits(String fullname) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_REPOSITORY_COMMITS, fullname)).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                setMethod(HttpClientFactory.METHOD_GET).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        CommitsListResponse response = new CommitsListResponse((List<Commit>)JsonTransformer.transform(client.getContent(), Commit[].class));
        response.setPageLinks(new PageLinks(client.getResponseHeader(HttpClientFactory.HEADER_LINK)));
        return response;
    }

    public static void getCommitsByUrl(String url, final GithubServiceListener<CommitsListResponse> listener) {
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
                CommitsListResponse response = new CommitsListResponse((List<Commit>)JsonTransformer.transform(content, Commit[].class));
                response.setPageLinks(new PageLinks(client.getResponseHeader(HttpClientFactory.HEADER_LINK)));

                listener.onSuccess(response);
            }
        });
    }
}
