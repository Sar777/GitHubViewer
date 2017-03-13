package instinctools.android.readers.json.transformers.github.comments;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.comments.Comment;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.utility.GitHubDate;

public class CommentTransformer implements ITransformer<Comment> {
    private static final String TAG = "CommentTransformer";

    private static final String J_URL = "url";
    private static final String J_HTML_URL = "html_url";
    private static final String J_ISSUE_URL = "issue_url";
    private static final String J_ID = "id";
    private static final String J_CREATED_AT = "created_at";
    private static final String J_UPDATED_AT = "updated_at";
    private static final String J_BODY = "body";
    private static final String J_COMMIT_ID = "commit_id";

    @Override
    public Comment transform(Object object) {
        JSONObject jsonObject;
        if (object instanceof JSONObject)
            jsonObject = (JSONObject)object;
        else if (object instanceof String) {
            try {
                jsonObject = new JSONObject((String)object);
            } catch (JSONException e) {
                Log.e(TAG, "Create json object error...", e);
                return null;
            }
        } else
            return null;

        Comment comment = new Comment();
        try {
            comment.setId(jsonObject.getInt(J_ID));
            comment.setUrl(jsonObject.getString(J_URL));
            comment.setHtmlUrl(jsonObject.getString(J_HTML_URL));

            if (jsonObject.has(J_ISSUE_URL))
                comment.setIssueUrl(jsonObject.getString(J_ISSUE_URL));

            if (jsonObject.has(J_COMMIT_ID))
                comment.setCommentId(jsonObject.getString(J_COMMIT_ID));

            comment.setBody(jsonObject.getString(J_BODY));
            comment.setCreatedAt(GitHubDate.parse(jsonObject.getString(J_CREATED_AT)));
            comment.setUpdatedAt(GitHubDate.parse(jsonObject.getString(J_UPDATED_AT)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return comment;
    }
}
