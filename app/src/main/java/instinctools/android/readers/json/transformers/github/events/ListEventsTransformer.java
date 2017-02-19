package instinctools.android.readers.json.transformers.github.events;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.events.Event;
import instinctools.android.readers.json.transformers.ITransformer;

public class ListEventsTransformer implements ITransformer<List<Event>> {
    private static final String TAG = "ListEventsTransformer";

    @Override
    public List<Event> transform(Object object) {
        JSONArray jsonArray;
        if (object instanceof String) {
            try {
                jsonArray = new JSONArray((String) object);
            } catch (JSONException e) {
                Log.e(TAG, "Create json object error...", e);
                return null;
            }
        } else if (object instanceof JSONArray)
            jsonArray = (JSONArray) object;
        else
            return new ArrayList<>();

        List<Event> events = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                events.add(new EventTransformer().transform(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json object fail", e);
            }
        }

        return events;
    }
}
