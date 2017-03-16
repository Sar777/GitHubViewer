package instinctools.android.readers.json.transformers.github.commits;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.commits.Commit;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.user.UserShortTransformer;

public class CommitTransformer implements ITransformer<Commit> {
    private static final String TAG = "CommitTransformer";

    private static final String J_URL = "url";
    private static final String J_SHA = "sha";
    private static final String J_COMMIT = "commit";
    private static final String J_AUTHOR = "author";
    private static final String J_COMMITTER = "committer";

    @Override
    public Commit transform(Object object) {
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

        Commit commit = new Commit();
        try {
            commit.setUrl(jsonObject.getString(J_URL));
            commit.setSha(jsonObject.getString(J_SHA));

            commit.setCommitInfo(new CommitInfoTransformer().transform(jsonObject.getJSONObject(J_COMMIT)));

            if (jsonObject.has(J_AUTHOR) && !jsonObject.isNull(J_AUTHOR))
                commit.setAuthor(new UserShortTransformer().transform(jsonObject.getJSONObject(J_AUTHOR)));

            if (jsonObject.has(J_COMMITTER) && !jsonObject.isNull(J_COMMITTER))
                commit.setCommitter(new UserShortTransformer().transform(jsonObject.getJSONObject(J_COMMITTER)));

        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return commit;
    }
}
