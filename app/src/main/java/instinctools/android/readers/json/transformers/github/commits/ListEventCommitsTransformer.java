package instinctools.android.readers.json.transformers.github.commits;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.commits.EventCommit;
import instinctools.android.readers.json.transformers.ITransformer;

public class ListEventCommitsTransformer implements ITransformer<List<EventCommit>> {
    private static final String TAG = "ListEventCommitsTrans";

    @Override
    public List<EventCommit> transform(Object object) {
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

        List<EventCommit> commits = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                commits.add(new EventCommitTransformer().transform(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json object fail", e);
            }
        }

        return commits;
    }
}
