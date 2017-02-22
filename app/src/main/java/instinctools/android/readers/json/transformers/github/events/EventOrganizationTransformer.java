package instinctools.android.readers.json.transformers.github.events;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.events.EventOrganization;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.utility.CustomTextUtils;

class EventOrganizationTransformer implements ITransformer<EventOrganization> {
    private static final String TAG = "EventOrganizationTrans";

    private static final String J_ID = "id";
    private static final String J_LOGIN = "login";
    private static final String J_GRAVATAR_ID = "gravatar_id";
    private static final String J_AVATAR_URL = "avatar_url";
    private static final String J_URL = "url";

    @Override
    public EventOrganization transform(Object object) {
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

        EventOrganization organization = new EventOrganization();
        try {
            organization.setId(jsonObject.getInt(J_ID));
            organization.setLogin(jsonObject.getString(J_LOGIN));
            organization.setAvatarUrl(jsonObject.getString(J_AVATAR_URL));
            organization.setGravatarId(CustomTextUtils.isEmpty(jsonObject.getString(J_GRAVATAR_ID)) ? "" : jsonObject.getString(J_GRAVATAR_ID));
            organization.setUrl(jsonObject.getString(J_URL));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return organization;
    }
}
