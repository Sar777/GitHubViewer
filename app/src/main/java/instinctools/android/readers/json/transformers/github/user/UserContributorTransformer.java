package instinctools.android.readers.json.transformers.github.user;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.user.UserContributor;
import instinctools.android.readers.json.transformers.ITransformer;

public class UserContributorTransformer implements ITransformer<UserContributor> {
    private static final String TAG = "UserContributorTrans";

    private static final String J_CONTRIBUTIONS = "contributions";

    @Override
    public UserContributor transform(Object object) {
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

        UserContributor contributor;
        try {
            contributor = new UserContributor(new UserShortTransformer().transform(jsonObject));
            contributor.setContributions(jsonObject.getInt(J_CONTRIBUTIONS));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return contributor;
    }
}
