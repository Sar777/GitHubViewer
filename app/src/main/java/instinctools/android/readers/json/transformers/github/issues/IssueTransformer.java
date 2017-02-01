package instinctools.android.readers.json.transformers.github.issues;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.issues.Issue;
import instinctools.android.models.github.issues.IssueState;
import instinctools.android.readers.json.transformers.ITransformer;

public class IssueTransformer implements ITransformer<Issue> {
    private static final String TAG = "IssueTransformer";

    private static final String J_ID = "id";
    private static final String J_URL = "url";
    private static final String J_HTML_URL = "html_url";
    private static final String J_REPOSITORY_URL = "repository_url";
    private static final String J_STATE = "state";
    private static final String J_NUMBER = "number";
    private static final String J_TITLE = "title";
    private static final String J_BODY = "body";
    private static final String J_LABELS = "labels";

    @Override
    public Issue transform(Object object) {
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

        Issue issue = new Issue();
        try {
            issue.setId(jsonObject.getInt(J_ID));
            issue.setUrl(jsonObject.getString(J_URL));
            issue.setHtmlUrl(jsonObject.getString(J_HTML_URL));
            issue.setRepositoryUrl(jsonObject.getString(J_REPOSITORY_URL));
            issue.setState(IssueState.get(jsonObject.getString(J_STATE)));
            issue.setNumber(jsonObject.getInt(J_NUMBER));
            issue.setTitle(jsonObject.getString(J_TITLE));
            issue.setBody(jsonObject.getString(J_BODY));
            issue.setLabels(new ListIssueLabelTransformer().transform(jsonObject.getJSONArray(J_LABELS)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return issue;
    }
}
