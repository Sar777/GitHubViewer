package instinctools.android.readers.json.transformers.github.events.payload;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.events.enums.PayloadActions;
import instinctools.android.models.github.events.payload.Payload;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.comments.CommentTransformer;
import instinctools.android.readers.json.transformers.github.events.payload.commit.ListPayloadCommitsTransformer;
import instinctools.android.readers.json.transformers.github.events.payload.release.EventPayloadReleaseTransformer;
import instinctools.android.readers.json.transformers.github.issues.IssueTransformer;
import instinctools.android.readers.json.transformers.github.pr.PullRequestTransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryTransformer;

public class EventPayloadTransformer implements ITransformer<Payload> {
    private static final String TAG = "EventPayloadTransformer";

    private static final String J_PUSH_ID = "push_id";
    private static final String J_SIZE = "size";
    private static final String J_REF = "ref";
    private static final String J_REF_TYPE = "ref_type";

    private static final String J_ACTION = "action";
    private static final String J_ISSUE = "issue";
    private static final String J_COMMENT = "comment";
    private static final String J_FORKEE = "forkee";
    private static final String J_COMMITS = "commits";
    private static final String J_PULL_REQUEST = "pull_request";
    private static final String J_RELEASE = "release";

    @Override
    public Payload transform(Object object) {
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

        Payload payload = new Payload();
        try {
            if (jsonObject.has(J_ACTION))
                payload.setAction(PayloadActions.get(jsonObject.getString(J_ACTION)));

            if (jsonObject.has(J_PUSH_ID))
                payload.setPushId(jsonObject.getInt(J_PUSH_ID));

            if (jsonObject.has(J_SIZE))
                payload.setSize(jsonObject.getInt(J_SIZE));

            if (jsonObject.has(J_REF))
                payload.setRef(jsonObject.getString(J_REF));

            if (jsonObject.has(J_REF_TYPE))
                payload.setRefType(jsonObject.getString(J_REF_TYPE));

            if (jsonObject.has(J_ISSUE))
                payload.setIssue(new IssueTransformer().transform(jsonObject.getJSONObject(J_ISSUE)));

            if (jsonObject.has(J_COMMENT))
                payload.setComment(new CommentTransformer().transform(jsonObject.getJSONObject(J_COMMENT)));

            if (jsonObject.has(J_FORKEE))
                payload.setForkee(new RepositoryTransformer().transform(jsonObject.getJSONObject(J_FORKEE)));

            if (jsonObject.has(J_COMMITS))
                payload.setCommits(new ListPayloadCommitsTransformer().transform(jsonObject.getJSONArray(J_COMMITS)));

            if (jsonObject.has(J_PULL_REQUEST))
                payload.setPullRequest(new PullRequestTransformer().transform(jsonObject.getJSONObject(J_PULL_REQUEST)));

            if (jsonObject.has(J_RELEASE))
                payload.setRelease(new EventPayloadReleaseTransformer().transform(jsonObject.getJSONObject(J_RELEASE)));

        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return payload;
    }
}
