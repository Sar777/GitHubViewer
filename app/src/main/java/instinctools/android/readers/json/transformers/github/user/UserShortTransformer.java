package instinctools.android.readers.json.transformers.github.user;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.user.UserShort;
import instinctools.android.readers.json.transformers.ITransformer;

public class UserShortTransformer implements ITransformer<UserShort> {
    private static final String TAG = "UserShortTransformer";

    private static final String J_ID = "id";
    private static final String J_LOGIN = "login";
    private static final String J_GRAVATAR_ID = "gravatar_id";
    private static final String J_AVATAR_URL = "avatar_url";
    private static final String J_URL = "url";
    private static final String J_HTML_URL = "html_url";
    private static final String J_TYPE = "type";

    @Override
    public UserShort transform(Object object) {
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

        UserShort userShort = new UserShort();
        try {
            userShort.setId(jsonObject.getInt(J_ID));
            userShort.setUrl(jsonObject.getString(J_URL));
            userShort.setGravatarId(jsonObject.getString(J_GRAVATAR_ID));
            userShort.setAvatarUrl(jsonObject.getString(J_AVATAR_URL));
            userShort.setLogin(jsonObject.getString(J_LOGIN));
            userShort.setHtmlUrl(jsonObject.getString(J_HTML_URL));
            userShort.setType(jsonObject.getString(J_TYPE));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return userShort;
    }
}
