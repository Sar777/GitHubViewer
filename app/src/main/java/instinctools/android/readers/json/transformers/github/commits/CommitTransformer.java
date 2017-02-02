package instinctools.android.readers.json.transformers.github.commits;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.commits.Commit;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.utility.GitHubDate;

public class CommitTransformer implements ITransformer<Commit> {
    private static final String TAG = "RepositoryTransformer";

    private static final String J_HASH = "hash";
    private static final String J_MESSAGE = "message";
    private static final String J_AUTHOR_ID = "author_id";
    private static final String J_AUTHOR_NAME = "author_name";
    private static final String J_AUTHOR_EMAIL = "author_email";
    private static final String J_AUTHOR_DATE = "author_date";
    private static final String J_COMMITTER_ID = "committer_id";
    private static final String J_COMMITTER_NAME = "committer_name";
    private static final String J_COMMITTER_EMAIL = "committer_email";
    private static final String J_COMMITTER_DATE = "committer_date";
    private static final String J_URL = "url";
    private static final String J_HTML_URL = "html_url";

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
            commit.setHash(jsonObject.getString(J_HASH));
            commit.setMessage(jsonObject.getString(J_MESSAGE));

            if (jsonObject.isNull(J_AUTHOR_ID))
                commit.setAuthorId(0);
            else
                commit.setAuthorId(jsonObject.getInt(J_AUTHOR_ID));

            commit.setAuthorName(jsonObject.getString(J_AUTHOR_NAME));
            commit.setAuthorEmail(jsonObject.getString(J_AUTHOR_EMAIL));
            commit.setAuthorDate(GitHubDate.parse(jsonObject.getString(J_AUTHOR_DATE)));
            commit.setCommitterDate(GitHubDate.parse(jsonObject.getString(J_COMMITTER_DATE)));

            if (jsonObject.isNull(J_COMMITTER_ID))
                commit.setCommitterId(0);
            else
                commit.setCommitterId(jsonObject.getInt(J_COMMITTER_ID));

            commit.setCommitterName(jsonObject.getString(J_COMMITTER_NAME));
            commit.setCommitterEmail(jsonObject.getString(J_COMMITTER_EMAIL));
            commit.setUrl(jsonObject.getString(J_URL));
            commit.setHtmlUrl(jsonObject.getString(J_HTML_URL));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return commit;
    }
}
