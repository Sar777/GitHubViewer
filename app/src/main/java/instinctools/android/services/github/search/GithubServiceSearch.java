package instinctools.android.services.github.search;

import java.net.HttpURLConnection;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.search.SearchRequest;
import instinctools.android.models.github.search.SearchResponse;
import instinctools.android.models.github.search.enums.SearchType;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.services.github.GithubService;
import instinctools.android.services.github.GithubServiceListener;

public class GithubServiceSearch extends GithubService {
    private static final String API_SEARCH = API_BASE_URL + "/search";

    public static SearchResponse getSearch(SearchRequest request) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_SEARCH + request.build()).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                setMethod(HttpClientFactory.METHOD_GET);

        // Added custom github header for only search by commits
        if (request.getType() == SearchType.COMMITS)
            client.addHeader(HttpClientFactory.HEADER_ACCEPT, HttpClientFactory.HEADER_ACCEPT_TYPE_CUSTOM_GITHUB);

        client.send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return new SearchResponse();

        SearchResponse searchResponse = JsonTransformer.transform(client.getContent(), SearchResponse.class);
        if (searchResponse == null) {
            return new SearchResponse();
        }

        searchResponse.setPageLinks(client.getResponseHeader(HttpClientFactory.HEADER_LINK));
        return searchResponse;
    }

    public static void getSearchByUrl(String url, final GithubServiceListener<SearchResponse> listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        final HttpClientFactory.HttpClient client = HttpClientFactory.
                create(url).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).
                setMethod(HttpClientFactory.METHOD_GET);

        client.addHeader(HttpClientFactory.HEADER_ACCEPT, HttpClientFactory.HEADER_ACCEPT_TYPE_CUSTOM_GITHUB);

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int code, String content) {
                listener.onError(code, (ErrorResponse) JsonTransformer.transform(content, ErrorResponse.class));
            }

            @Override
            public void onSuccess(int code, String content) {
                SearchResponse searchResponse = JsonTransformer.transform(content, SearchResponse.class);
                if (searchResponse != null)
                    searchResponse.setPageLinks(client.getResponseHeader(HttpClientFactory.HEADER_LINK));

                listener.onSuccess(searchResponse);
            }
        });
    }
}
