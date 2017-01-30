package instinctools.android.readers.json.transformers.github.notification;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.notification.Notification;
import instinctools.android.models.github.notification.NotificationRepository;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryOwnerTransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryTransformer;
import instinctools.android.utility.CustomTextUtils;

public class NotificationRepositoryTransformer implements ITransformer<NotificationRepository> {
    private static final String TAG = "NotificationRepoTrans";

    private static final String J_ID = "id";
    private static final String J_NAME = "name";
    private static final String J_HTML_URL = "html_url";
    private static final String J_URL = "url";
    private static final String J_FULLNAME = "full_name";
    private static final String J_REPOSITORY_OWNER = "owner";
    private static final String J_IS_PRIVATE = "private";
    private static final String J_IS_FORK = "fork";
    private static final String J_DESCRIPTION = "description";

    @Override
    public NotificationRepository transform(Object object) {
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

        NotificationRepository repository = new NotificationRepository();
        try {
            repository.setId(jsonObject.getInt(J_ID));
            repository.setName(jsonObject.getString(J_NAME));
            repository.setHtmlUrl(jsonObject.getString(J_HTML_URL));
            repository.setUrl(jsonObject.getString(J_URL));
            repository.setFullName(jsonObject.getString(J_FULLNAME));
            repository.setDescription(CustomTextUtils.isEmpty(jsonObject.getString(J_DESCRIPTION)) ? "Empty" : jsonObject.getString(J_DESCRIPTION));
            repository.setPrivate(jsonObject.getBoolean(J_IS_PRIVATE));
            repository.setFork(jsonObject.getBoolean(J_IS_FORK));
            repository.setOwner(new NotificationRepositoryOwnerTransformer().transform(jsonObject.getJSONObject(J_REPOSITORY_OWNER)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return repository;
    }
}
