package instinctools.android.readers.json.transformers.github.events.payload;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.events.payload.PayloadCommit;
import instinctools.android.readers.json.transformers.ITransformer;

public class EventPayloadCommitTransformer implements ITransformer<PayloadCommit> {
    private static final String TAG = "EventPayloadCommitTrans";

    private static final String J_SHA = "sha";
    private static final String J_AUTHOR = "author";
    private static final String J_MESSAGE = "message";
    private static final String J_URL = "url";

    @Override
    public PayloadCommit transform(Object object) {
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

        PayloadCommit payloadCommit = new PayloadCommit();
        try {
            payloadCommit.setSha(jsonObject.getString(J_SHA));
            payloadCommit.setMessage(jsonObject.getString(J_MESSAGE));
            payloadCommit.setUrl(jsonObject.getString(J_URL));

            payloadCommit.setAuthor(new EventPayloadCommitAuthorTransformer().transform(jsonObject.getJSONObject(J_AUTHOR)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return payloadCommit;
    }
}
