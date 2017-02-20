package instinctools.android.readers.json.transformers.github.commits;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.commits.CommitInfo;
import instinctools.android.readers.json.transformers.ITransformer;

public class CommitInfoTransformer implements ITransformer<CommitInfo> {
    private static final String TAG = "CommitInfoTransformer";

    private static final String J_URL = "url";
    private static final String J_AUTHOR = "author";
    private static final String J_COMMITTER = "committer";
    private static final String J_MESSAGE = "message";
    private static final String J_COMMENT_COUNT = "comment_count";

    @Override
    public CommitInfo transform(Object object) {
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

        CommitInfo commitInfo = new CommitInfo();
        try {
            commitInfo.setUrl(jsonObject.getString(J_URL));

            commitInfo.setAuthor(new CommitInfoAuthorDataTransformer().transform(jsonObject.getJSONObject(J_AUTHOR)));
            commitInfo.setCommitter(new CommitInfoAuthorDataTransformer().transform(jsonObject.getJSONObject(J_COMMITTER)));

            commitInfo.setMessage(jsonObject.getString(J_MESSAGE));
            commitInfo.setCommentCount(jsonObject.getInt(J_COMMENT_COUNT));

        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return commitInfo;
    }
}
