package instinctools.android.readers.json.transformers.github.search;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.search.SearchResponse;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.commits.ListEventCommitsTransformer;
import instinctools.android.readers.json.transformers.github.issues.ListIssueTransformer;
import instinctools.android.readers.json.transformers.github.repository.ListRepositoriesTransformer;

public class SearchResponseTransformer implements ITransformer<SearchResponse> {
    private static final String TAG = "SearchResponseTrans";

    private static final String J_TOTAL_COUNT = "total_count";
    private static final String J_INCOMPLETE_RESULTS = "incomplete_results";
    private static final String J_ITEMS = "items";

    private static final String J_HAS_TYPE_REPOS = "default_branch";
    private static final String J_HAS_TYPE_COMMITS = "committer_id";
    private static final String J_HAS_TYPE_ISSUES = "assignee";
    private static final String J_HAS_TYPE_USERS = "login";

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
            if (searchResponse.getTotalCount() == 0)
                return searchResponse;

            JSONObject tempObject = (JSONObject) jsonObject.getJSONArray(J_ITEMS).get(0);

            // Is commits
            if (tempObject.has(J_HAS_TYPE_COMMITS))
                searchResponse.setRepositories(new ListEventCommitsTransformer().transform(jsonObject.getJSONArray(J_ITEMS)));
            // Is repositories
            else if (tempObject.has(J_HAS_TYPE_REPOS))
                searchResponse.setRepositories(new ListRepositoriesTransformer().transform(jsonObject.getJSONArray(J_ITEMS)));
            // Is issues
            else if (tempObject.has(J_HAS_TYPE_ISSUES))
                searchResponse.setRepositories(new ListIssueTransformer().transform(jsonObject.getJSONArray(J_ITEMS)));
            // Is users
            else if (tempObject.has(J_HAS_TYPE_USERS))
                searchResponse.setRepositories(new SearchListUsersTransformer().transform(jsonObject.getJSONArray(J_ITEMS)));
            else
                throw new UnsupportedOperationException("Unsupported search response items type");

        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return searchResponse;
    }
}
