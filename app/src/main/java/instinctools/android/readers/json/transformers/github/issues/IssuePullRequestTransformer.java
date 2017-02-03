package instinctools.android.readers.json.transformers.github.issues;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.issues.IssuePullRequest;
import instinctools.android.readers.json.transformers.ITransformer;

public class IssuePullRequestTransformer implements ITransformer<IssuePullRequest> {
    private static final String TAG = "IssueTransformer";

    private static final String J_URL = "url";
    private static final String J_HTML_URL = "html_url";
    private static final String J_DIFF_URL = "diff_url";
    private static final String J_PATCH_URL = "patch_url";

    @Override
    public IssuePullRequest transform(Object object) {
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

        IssuePullRequest pullRequest = new IssuePullRequest();
        try {
            pullRequest.setUrl(jsonObject.getString(J_URL));
            pullRequest.setHtmlUrl(jsonObject.getString(J_HTML_URL));
            pullRequest.setDiffUrl(jsonObject.getString(J_DIFF_URL));
            pullRequest.setPatchUrl(jsonObject.getString(J_PATCH_URL));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return pullRequest;
    }
}
