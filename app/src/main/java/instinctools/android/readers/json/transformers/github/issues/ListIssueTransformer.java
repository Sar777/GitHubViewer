package instinctools.android.readers.json.transformers.github.issues;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.issues.Issue;
import instinctools.android.readers.json.transformers.ITransformer;

public class ListIssueTransformer implements ITransformer<List<Issue>> {
    private static final String TAG = "ListIssueTransformer";

    @Override
    public List<Issue> transform(Object object) {
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

        List<Issue> issues = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); ++i)
                issues.add(new IssueTransformer().transform(jsonArray.get(i)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return issues;
    }
}
