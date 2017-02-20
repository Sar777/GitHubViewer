package instinctools.android.readers.json.transformers.github.commits;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.commits.CommitInfoAuthor;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.utility.GitHubDate;

public class CommitInfoAuthorDataTransformer implements ITransformer<CommitInfoAuthor> {
    private static final String TAG = "CommitInfoAutDataTrans";

    private static final String J_NAME = "name";
    private static final String J_EMAIL = "email";
    private static final String J_DATE = "date";

    @Override
    public CommitInfoAuthor transform(Object object) {
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

        CommitInfoAuthor commitInfoAuthor = new CommitInfoAuthor();
        try {
            commitInfoAuthor.setName(jsonObject.getString(J_NAME));
            commitInfoAuthor.setEmail(jsonObject.getString(J_EMAIL));
            commitInfoAuthor.setDate(GitHubDate.parse(jsonObject.getString(J_DATE)));

        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return commitInfoAuthor;
    }
}
