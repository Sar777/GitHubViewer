package instinctools.android.readers.json.transformers.github.events;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.events.EventAuthor;
import instinctools.android.readers.json.transformers.ITransformer;

public class EventAuthorTransformer implements ITransformer<EventAuthor> {
    private static final String TAG = "EventAuthorTransformer";

    private static final String J_ID = "id";
    private static final String J_LOGIN = "login";
    private static final String J_GRAVATAR_ID = "gravatar_id";
    private static final String J_AVATAR_URL = "avatar_url";
    private static final String J_URL = "url";
    private static final String J_HTML_URL = "html_url";
    private static final String J_TYPE = "type";

    @Override
    public EventAuthor transform(Object object) {
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

        EventAuthor eventAuthor = new EventAuthor();
        try {
            eventAuthor.setId(jsonObject.getInt(J_ID));
            eventAuthor.setUrl(jsonObject.getString(J_URL));
            eventAuthor.setGravatarId(jsonObject.getString(J_GRAVATAR_ID));
            eventAuthor.setAvatarUrl(jsonObject.getString(J_AVATAR_URL));
            eventAuthor.setLogin(jsonObject.getString(J_LOGIN));
            eventAuthor.setHtmlUrl(jsonObject.getString(J_HTML_URL));
            eventAuthor.setType(jsonObject.getString(J_TYPE));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return eventAuthor;
    }
}
