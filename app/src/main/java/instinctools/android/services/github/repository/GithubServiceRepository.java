package instinctools.android.services.github.repository;

import java.net.HttpURLConnection;
import java.util.List;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.repositories.RepositoryReadme;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryReadmeTransformer;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.GithubServices;

public class GithubServiceRepository extends GithubServices {

    private static final String API_REPO_URL = API_BASE_URL + "/user/repos";
    private static final String API_REPOSITORY_README_URL = API_BASE_URL + "/repos/%s/%s/readme";

    public static List<Repository> getRepositoryList() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_REPO_URL).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return JsonTransformer.transform(client.getContent(), Repository[].class);
    }

    public static RepositoryReadme getRepositoryReadme(String owner, String repo) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_REPOSITORY_README_URL, owner, repo)).
                setMethod(HttpClientFactory.METHOD_GET).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return (RepositoryReadme) JsonTransformer.transform(client.getContent(), RepositoryReadmeTransformer.class);
    }

    public static void getRepositoryReadme(String owner, String repo, final GithubServiceListener<RepositoryReadme> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubServices.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(String.format(API_REPOSITORY_README_URL, owner, repo)).
                setMethod(HttpClientFactory.METHOD_GET);

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode) {
                listener.onError(errCode);
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
}
