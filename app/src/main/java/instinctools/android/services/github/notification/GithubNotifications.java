package instinctools.android.services.github.notification;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;

import instinctools.android.http.HttpClientFactory;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.models.github.repositories.RepositoryReadme;
import instinctools.android.readers.json.JsonTransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryReadmeTransformer;
import instinctools.android.services.github.GithubService;

public class GithubNotifications extends GithubService {
    private static final String TAG = "GithubNotifications";

    private static final String API_NOTIFICATIONS = API_BASE_URL + "/notifications";

    private static final String FIELD_ALL = "all";
    private static final String FIELD_PARTICIPATING = "participating";

    private static String getNotificationData(boolean all, boolean participating) {
        JSONObject object = new JSONObject();
        try {
            object.put(FIELD_ALL, all);
            object.put(FIELD_PARTICIPATING, participating);
        } catch (JSONException e) {
            Log.e(TAG, "Fail build notification request data");
            return null;
        }
        return object.toString();
    }

    public static List<Notification> getNotifications(boolean all, boolean participating) {
        if (mSessionStorage == null)
            throw new IllegalArgumentException("Not init github service. Please, before use it: GithubService.init");

        HttpClientFactory.HttpClient client = HttpClientFactory.
                create(API_NOTIFICATIONS).
                setData(getNotificationData(all, participating)).
                setMethod(HttpClientFactory.METHOD_GET).
                addHeader(HttpClientFactory.HEADER_AUTHORIZATION, getFormatAccessToken()).send();

        if (client.getCode() != HttpURLConnection.HTTP_OK)
            return null;

        return (List<Notification>) JsonTransformer.transform(client.getContent(), Notification[].class);
    }

}
