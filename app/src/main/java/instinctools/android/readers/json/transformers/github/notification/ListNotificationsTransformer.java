package instinctools.android.readers.json.transformers.github.notification;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.notification.Notification;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryTransformer;

public class ListNotificationsTransformer implements ITransformer<List<Notification>> {
    private static final String TAG = "ListUserRepositories";

    @Override
    public List<Notification> transform(Object object) {
        JSONArray jsonArray;
        if (object instanceof String) {
            try {
                jsonArray = new JSONArray((String) object);
            } catch (JSONException e) {
                Log.e(TAG, "Create json object error...", e);
                return null;
            }
        } else if (object instanceof JSONArray)
            jsonArray = (JSONArray) object;
        else
            return new ArrayList<>();

        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i)
            try {
                notifications.add(new NotificationTransformer().transform(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json object fail", e);
            }

        return notifications;
    }
}
