package instinctools.android.readers.json.transformers.github.contents;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.contents.Content;
import instinctools.android.readers.json.transformers.ITransformer;

public class ListContentTransformer implements ITransformer<List<Content>> {
    private static final String TAG = "ListContentTransformer";

    @Override
    public List<Content> transform(Object object) {
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

        List<Content> contents = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i)
            try {
                contents.add(new ContentTransformer().transform(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json object fail", e);
            }

        return contents;
    }
}
