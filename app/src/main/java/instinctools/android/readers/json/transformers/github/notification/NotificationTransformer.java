package instinctools.android.readers.json.transformers.github.notification;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.notification.Notification;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryTransformer;

public class NotificationTransformer implements ITransformer<Notification> {
    private static final String TAG = "NotificationTransformer";

    private static final String J_ID = "id";
    private static final String J_REPOSITORY = "repository";
    private static final String J_SUBJECT = "subject";
    private static final String J_REASON = "reason";
    private static final String J_UNREAD = "unread";
    private static final String J_UPDATED_AT = "updated_at";
    private static final String J_LAST_READ_AT = "last_read_at";
    private static final String J_URL = "url";

    @Override
    public Notification transform(Object object) {
        JSONObject jsonObject;
        if (object instanceof String) {
            try {
                jsonObject = new JSONObject((String) object);
            } catch (JSONException e) {
                Log.e(TAG, "Create json object error...", e);
                return null;
            }
        } else if (object instanceof JSONObject)
            jsonObject = (JSONObject) object;
        else
            return null;

        Notification notification = new Notification();
        try {
            notification.setId(jsonObject.getInt(J_ID));
            notification.setRepository(new NotificationRepositoryTransformer().transform(jsonObject.getJSONObject(J_REPOSITORY)));
            notification.setSubject(new NotificationSubjectTransformer().transform(jsonObject.getJSONObject(J_SUBJECT)));
            notification.setReason(jsonObject.getString(J_REASON));
            notification.setUnread(jsonObject.getBoolean(J_UNREAD));
            notification.setUpdateAt(jsonObject.getString(J_UPDATED_AT));
            notification.setLastReadAt(jsonObject.getString(J_LAST_READ_AT));
            notification.setUrl(jsonObject.getString(J_URL));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return notification;
    }
}
