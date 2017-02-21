package instinctools.android.readers.json.transformers.github.issues;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.issues.Issue;
import instinctools.android.models.github.issues.IssueState;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.user.ListUsersShortTransformer;
import instinctools.android.readers.json.transformers.github.user.UserShortTransformer;
import instinctools.android.utility.GitHubDate;

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
    private static final String J_USER = "user";
    private static final String J_ASSIGNEE = "assignee";
    private static final String J_COMMENTS = "comments";
    private static final String J_PULL_REQUEST = "pull_request";
    private static final String J_LABELS = "labels";
    private static final String J_CREATED_AT = "created_at";
    private static final String J_UPDATED_AT = "updated_at";
    private static final String J_CLOSED_AT = "closed_at";
    private static final String J_ASSIGNEES = "assignees";

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
            issue.setUser(new UserShortTransformer().transform(jsonObject.getJSONObject(J_USER)));

            if (!jsonObject.isNull(J_ASSIGNEE))
                issue.setAssignee(new UserShortTransformer().transform(jsonObject.getJSONObject(J_ASSIGNEE)));

            issue.setComments(jsonObject.getInt(J_COMMENTS));
            issue.setLabels(new ListIssueLabelTransformer().transform(jsonObject.getJSONArray(J_LABELS)));
            issue.setCreatedAt(GitHubDate.parse(jsonObject.getString(J_CREATED_AT)));
            issue.setAssignees(new ListUsersShortTransformer().transform(jsonObject.getJSONArray(J_ASSIGNEES)));

            if (!jsonObject.isNull(J_UPDATED_AT))
                issue.setUpdateAt(GitHubDate.parse(jsonObject.getString(J_UPDATED_AT)));

            if (!jsonObject.isNull(J_CLOSED_AT))
                issue.setClosedAt(GitHubDate.parse(jsonObject.getString(J_CLOSED_AT)));

            if (!jsonObject.isNull(J_PULL_REQUEST))
                issue.setPullRequest(new IssuePullRequestTransformer().transform(jsonObject.getJSONObject(J_PULL_REQUEST)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return issue;
    }
}
