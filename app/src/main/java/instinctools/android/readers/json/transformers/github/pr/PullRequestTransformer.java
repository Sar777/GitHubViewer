package instinctools.android.readers.json.transformers.github.pr;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.pr.PullRequest;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.utility.GitHubDate;

public class PullRequestTransformer implements ITransformer<PullRequest> {
    private static final String TAG = "PullRequestTransformer";

    private static final String J_ID = "id";
    private static final String J_URL = "url";
    private static final String J_HTML_URL = "html_url";
    private static final String J_NUMBER = "number";
    private static final String J_TITLE = "title";
    private static final String J_STATE = "state";
    private static final String J_LOCKED = "locked";
    private static final String J_CREATED_AT = "created_at";
    private static final String J_CLOSED_AT = "closed_at";
    private static final String J_MERGED_AT = "merged_at";

    @Override
    public PullRequest transform(Object object) {
        JSONObject jsonObject;
        if (object instanceof String) {
            try {
                jsonObject = new JSONObject((String) object);
            } catch (JSONException e) {
                Log.e(TAG, "Create json object error...", e);
                return null;
            }
        } else if (object instanceof JSONObject)
            jsonObject = (JSONObject) object;
        else
            return null;

        PullRequest pullRequest = new PullRequest();
        try {
            pullRequest.setId(jsonObject.getInt(J_ID));
            pullRequest.setUrl(jsonObject.getString(J_URL));
            pullRequest.setNumber(jsonObject.getInt(J_NUMBER));
            pullRequest.setHtmlUrl(jsonObject.getString(J_HTML_URL));
            pullRequest.setTitle(jsonObject.getString(J_TITLE));
            pullRequest.setState(jsonObject.getString(J_STATE));
            pullRequest.setLocked(jsonObject.getBoolean(J_LOCKED));
            pullRequest.setCreatedAt(GitHubDate.parse(jsonObject.getString(J_CREATED_AT)));

            if (!jsonObject.isNull(J_CLOSED_AT))
                pullRequest.setClosedAt(GitHubDate.parse(jsonObject.getString(J_CLOSED_AT)));

            if (!jsonObject.isNull(J_MERGED_AT))
                pullRequest.setMergedAt(GitHubDate.parse(jsonObject.getString(J_MERGED_AT)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return pullRequest;
    }
}
