package instinctools.android.readers.json.transformers.github.events;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.events.EventRepository;
import instinctools.android.readers.json.transformers.ITransformer;

class EventRepoTransformer implements ITransformer<EventRepository> {
    private static final String TAG = "EventRepoTransformer";

    private static final String J_ID = "id";
    private static final String J_NAME = "name";
    private static final String J_URL = "url";

    @Override
    public EventRepository transform(Object object) {
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

        EventRepository repository = new EventRepository();
        try {
            repository.setId(jsonObject.getInt(J_ID));
            repository.setName(jsonObject.getString(J_NAME));
            repository.setUrl(jsonObject.getString(J_URL));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return repository;
    }
}
