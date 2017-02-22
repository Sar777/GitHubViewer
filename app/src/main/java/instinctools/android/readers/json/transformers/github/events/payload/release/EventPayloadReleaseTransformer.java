package instinctools.android.readers.json.transformers.github.events.payload.release;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.events.payload.release.PayloadRelease;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.user.UserShortTransformer;

public class EventPayloadReleaseTransformer implements ITransformer<PayloadRelease> {
    private static final String TAG = "EventPayloadRelTrans";

    private static final String J_ID = "id";
    private static final String J_URL = "url";
    private static final String J_HTML_URL = "html_url";
    private static final String J_TAG_NAME = "tag_name";
    private static final String J_NAME = "name";
    private static final String J_AUTHOR = "author";
    private static final String J_ASSETS = "assets";

    @Override
    public PayloadRelease transform(Object object) {
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

        PayloadRelease payloadRelease = new PayloadRelease();
        try {
            payloadRelease.setId(jsonObject.getInt(J_ID));
            payloadRelease.setUrl(jsonObject.getString(J_URL));
            payloadRelease.setHtmlUrl(jsonObject.getString(J_HTML_URL));
            payloadRelease.setTagName(jsonObject.getString(J_TAG_NAME));
            payloadRelease.setName(jsonObject.getString(J_NAME));

            payloadRelease.setAuthor(new UserShortTransformer().transform(jsonObject.getJSONObject(J_AUTHOR)));
            payloadRelease.setAssets(new ListEventPayloadReleaseAssetTransformer().transform(jsonObject.getJSONArray(J_ASSETS)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return payloadRelease;
    }
}
