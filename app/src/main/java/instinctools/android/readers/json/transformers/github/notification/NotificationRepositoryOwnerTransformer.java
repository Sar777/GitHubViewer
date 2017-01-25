package instinctools.android.readers.json.transformers.github.notification;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.notification.NotificationRepositoryOwner;
import instinctools.android.models.github.repositories.RepositoryOwner;
import instinctools.android.readers.json.transformers.ITransformer;

public class NotificationRepositoryOwnerTransformer implements ITransformer<NotificationRepositoryOwner> {
    private static final String TAG = "NotifRepoOwnerTrans";

    private static final String J_LOGIN = "login";
    private static final String J_ID = "id";
    private static final String J_URL = "url";
    private static final String J_AVATAR_URL = "avatar_url";

    @Override
    public NotificationRepositoryOwner transform(Object object) {
        if (!(object instanceof JSONObject))
            return null;

        JSONObject jsonObject = (JSONObject)object;

        NotificationRepositoryOwner owner = new NotificationRepositoryOwner();
        try {
            owner.setId(jsonObject.getInt(J_ID));
            owner.setLogin(jsonObject.getString(J_LOGIN));
            owner.setUrl(jsonObject.getString(J_URL));
            owner.setAvatarUrl(jsonObject.getString(J_AVATAR_URL));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return owner;
    }
}
