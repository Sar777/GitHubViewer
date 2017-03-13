package instinctools.android.readers.json.transformers.github.events.payload.commit;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.events.payload.commit.PayloadCommit;
import instinctools.android.readers.json.transformers.ITransformer;

public class ListPayloadCommitsTransformer implements ITransformer<List<PayloadCommit>> {
    private static final String TAG = "ListPayloadCommitsTrans";

    @Override
    public List<PayloadCommit> transform(Object object) {
        JSONArray jsonArray;
        if (object instanceof String) {
            try {
                jsonArray = new JSONArray((String) object);
            } catch (JSONException e) {
                Log.e(TAG, "Create json object error...", e);
                return null;
            }
        } else if (object instanceof JSONArray)
            jsonArray = (JSONArray) object;
        else
            return new ArrayList<>();

        List<PayloadCommit> payloadCommits = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); ++i)
                payloadCommits.add(new EventPayloadCommitTransformer().transform(jsonArray.get(i)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return payloadCommits;
    }
}
