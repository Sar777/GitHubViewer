package instinctools.android.readers.json.transformers.github.events.payload.commit;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.events.payload.commit.PayloadCommitAuthor;
import instinctools.android.readers.json.transformers.ITransformer;

class EventPayloadCommitAuthorTransformer implements ITransformer<PayloadCommitAuthor> {
    private static final String TAG = "EventPayCommAuTrans";

    private static final String J_EMAIL = "email";
    private static final String J_NAME = "name";

    @Override
    public PayloadCommitAuthor transform(Object object) {
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

        PayloadCommitAuthor payloadCommitAuthor = new PayloadCommitAuthor();
        try {
            payloadCommitAuthor.setEmail(jsonObject.getString(J_EMAIL));
            payloadCommitAuthor.setName(jsonObject.getString(J_NAME));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return payloadCommitAuthor;
    }
}
