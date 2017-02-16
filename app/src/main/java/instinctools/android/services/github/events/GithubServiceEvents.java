package instinctools.android.services.github.events;

import java.net.HttpURLConnection;
import java.util.List;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.PageLinks;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.events.Event;
import instinctools.android.models.github.events.EventsListResponse;
import instinctools.android.models.github.search.SearchResponse;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.services.github.GithubService;
import instinctools.android.services.github.GithubServiceListener;

public class GithubServiceEvents extends GithubService {
    private static final String TAG = "GithubServiceEvents";

    private static final String API_EVENTS = API_BASE_URL + "/events";

    public static EventsListResponse getEventsResponse() {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_EVENTS).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        EventsListResponse response = new EventsListResponse((List<Event>)JsonTransformer.transform(client.getContent(), Event[].class));
        response.setPageLinks(new PageLinks(client.getResponseHeader(HttpClientFactory.HEADER_LINK)));
        return response;
    }

    public static void getEventsByUrl(String url, final GithubServiceListener<EventsListResponse> listener) {
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
                EventsListResponse eventListResponse = JsonTransformer.transform(content, SearchResponse.class);
                if (eventListResponse != null)
                    eventListResponse.setPageLinks(new PageLinks(client.getResponseHeader(HttpClientFactory.HEADER_LINK)));

                listener.onSuccess(eventListResponse);
            }
        });
    }
}
