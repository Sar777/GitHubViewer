package instinctools.android.services.github.search;

import java.net.HttpURLConnection;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.search.SearchRequest;
import instinctools.android.models.github.search.SearchResponse;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.services.github.GithubService;
import instinctools.android.services.github.GithubServiceListener;

public class GithubServiceSearch extends GithubService {
    private static final String API_SEARCH_REPOSITORY = API_BASE_URL + "/search/repositories";

    public static void getSearchRepository(SearchRequest request, final GithubServiceListener<SearchResponse> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_SEARCH_REPOSITORY + request).
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

                SearchResponse searchResponse = JsonTransformer.transform(content, SearchResponse.class);
                if (searchResponse == null) {
                    listener.onSuccess(new SearchResponse());
                    return;
                }
                listener.onSuccess(searchResponse);

            }
        });
    }

    public static SearchResponse getSearchRepository(SearchRequest request) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_SEARCH_REPOSITORY + request).
                setMethod(HttpClientFactory.METHOD_GET).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return new SearchResponse();

        SearchResponse searchResponse = JsonTransformer.transform(client.getContent(), SearchResponse.class);
        if (searchResponse == null)
            return new SearchResponse();

        return searchResponse;
    }
}
