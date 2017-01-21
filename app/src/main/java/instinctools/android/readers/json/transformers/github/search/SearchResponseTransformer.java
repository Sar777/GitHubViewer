package instinctools.android.readers.json.transformers.github.search;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.search.SearchResponse;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.repository.ListUserRepositoriesTransformer;

public class SearchResponseTransformer implements ITransformer<SearchResponse> {
    private static final String TAG = "SearchResponseTrans";

    private static final String J_TOTAL_COUNT = "total_count";
    private static final String J_INCOMPLETE_RESULTS = "incomplete_results";
    private static final String J_ITEMS = "items";

    @Override
    public SearchResponse transform(Object object) {
        if (!(object instanceof String))
            return null;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject((String)object);
        } catch (JSONException e) {
            Log.e(TAG, "Create json object error...", e);
            return null;
        }

        SearchResponse searchResponse = new SearchResponse();
        try {
            searchResponse.setTotalCount(jsonObject.getInt(J_TOTAL_COUNT));
            searchResponse.setIncompleteResults(jsonObject.getBoolean(J_INCOMPLETE_RESULTS));
            searchResponse.setRepositories(new ListUserRepositoriesTransformer().transform(jsonObject.getJSONArray(J_ITEMS)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return searchResponse;
    }
}
