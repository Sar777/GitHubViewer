package instinctools.android.services.github.notification;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.services.github.GithubService;
import instinctools.android.services.github.GithubServiceListener;

public class GithubNotifications extends GithubService {
    private static final String TAG = "GithubNotifications";

    private static final String API_NOTIFICATIONS = API_BASE_URL + "/notifications";

    private static final String FIELD_ALL = "all";
    private static final String FIELD_PARTICIPATING = "participating";

    private static String getNotificationFormat(boolean all, boolean participating) {
        return "?" +
                FIELD_ALL +
                "=" +
                all +
                "&" +
                FIELD_PARTICIPATING +
                "=" +
                participating;
    }

    public static List<Notification> getNotifications(boolean all, boolean participating) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_NOTIFICATIONS + getNotificationFormat(all, participating)).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return (List<Notification>) JsonTransformer.transform(client.getContent(), Notification[].class);
    }

    public static void markNotification(String url, final GithubServiceListener listener) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                        create(url).
                        setMethod(HttpClientFactory.METHOD_PATCH).
                        addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken());

        client.send(new OnHttpClientListener() {
            @Override
            public void onError(int errCode) {
                listener.onError(errCode);
            }

            @Override
            public void onSuccess(int code, String content) {
                listener.onSuccess(code == HttpURLConnection.HTTP_RESET);
            }
        });
    }
}
