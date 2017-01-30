package instinctools.android.readers.json.transformers.github.notification;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.notification.NotificationSubject;
import instinctools.android.models.github.notification.NotificationType;
import instinctools.android.readers.json.transformers.ITransformer;

public class NotificationSubjectTransformer implements ITransformer<NotificationSubject> {
    private static final String TAG = "NotificationSubTrans";

    private static final String J_TITLE = "title";
    private static final String J_URL = "url";
    private static final String J_LATEST_COMMENT_URL = "latest_comment_url";
    private static final String J_TYPE = "type";

    @Override
    public NotificationSubject transform(Object object) {
        JSONObject jsonObject;
        if (object instanceof JSONObject)
            jsonObject = (JSONObject)object;
        else if (object instanceof String) {
            try {
                jsonObject = new JSONObject((String)object);
            } catch (JSONException e) {
                Log.e(TAG, "Create json object error...", e);
                return null;
            }
        } else
            return null;

        NotificationSubject notificationSubject = new NotificationSubject();
        try {
            notificationSubject.setTitle(jsonObject.getString(J_TITLE));
            notificationSubject.setType(NotificationType.get(jsonObject.getString(J_TYPE)));
            notificationSubject.setLatestCommentUrl(jsonObject.getString(J_LATEST_COMMENT_URL));
            notificationSubject.setUrl(jsonObject.getString(J_URL));
         } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return notificationSubject;
    }
}
