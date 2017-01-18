package instinctools.android.readers.json.transformers.github.authorization;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.authorization.AppToken;
import instinctools.android.readers.json.transformers.ITransformer;

/**
 * Created by orion on 12.1.17.
 */

public class AppTokenTransformer implements ITransformer<AppToken> {
    private static final String TAG = "AppTokenTransformer";

    private static final String J_URL = "url";
    private static final String J_NAME = "name";
    private static final String J_CLIENT_ID = "client_id";

    @Override
    public AppToken transform(Object object) {
        if (!(object instanceof JSONObject))
            return null;

        JSONObject jsonObject = (JSONObject)object;

        AppToken appToken = new AppToken();

        try {
            appToken.setUrl(jsonObject.getString(J_URL));
            appToken.setName(jsonObject.getString(J_NAME));
            appToken.setClientId(jsonObject.getString(J_CLIENT_ID));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return appToken;
    }
}
