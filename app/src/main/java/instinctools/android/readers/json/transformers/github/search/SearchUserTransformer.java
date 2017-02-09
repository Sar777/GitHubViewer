package instinctools.android.readers.json.transformers.github.search;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.user.User;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.utility.CustomTextUtils;

public class SearchUserTransformer implements ITransformer<User> {
    private static final String TAG = "SearchUserTransformer";

    private static final String J_LOGIN = "login";
    private static final String J_ID = "id";
    private static final String J_AVATAR_URL = "avatar_url";
    private static final String J_GRAVATAR_ID = "gravatar_id";
    private static final String J_TYPE = "type";

    @Override
    public User transform(Object object) {
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

        User user = new User();
        try {
            user.setId(jsonObject.getInt(J_ID));
            user.setAvatarUrl(CustomTextUtils.isEmpty(jsonObject.getString(J_AVATAR_URL)) ? "" : jsonObject.getString(J_AVATAR_URL));
            user.setLogin(jsonObject.getString(J_LOGIN));
            user.setGravatarId(CustomTextUtils.isEmpty(jsonObject.getString(J_GRAVATAR_ID)) ? "" : jsonObject.getString(J_GRAVATAR_ID));
            user.setType(jsonObject.getString(J_TYPE));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return user;
    }
}
