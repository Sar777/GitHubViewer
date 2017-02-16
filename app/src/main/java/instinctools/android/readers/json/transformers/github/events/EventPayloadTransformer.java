package instinctools.android.readers.json.transformers.github.events;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.events.Payload;
import instinctools.android.readers.json.transformers.ITransformer;

public class EventPayloadTransformer implements ITransformer<Payload> {
    private static final String TAG = "EventPayloadTransformer";

    private static final String J_ID = "id";

    @Override
    public Payload transform(Object object) {
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

        Payload payload = new Payload();
//        try {
//        } catch (JSONException e) {
//            Log.e(TAG, "Parse json field error...", e);
//            return null;
//        }

        return payload;
    }
}
