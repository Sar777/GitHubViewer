package instinctools.android.readers.json.transformers.github.authorization;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.authorization.AccessToken;
import instinctools.android.readers.json.transformers.ITransformer;

/**
 * Created by orion on 12.1.17.
 */

public class AccessTokenTransformer implements ITransformer<AccessToken> {
    private static final String TAG = "AccessTokenTransformer";

    private static final String J_ACCESS_TOKEN = "access_token";
    private static final String J_TOKEN_TYPE = "token_type";
    private static final String J_SCOPE = "scope";

    @Override
    public AccessToken transform(Object object) {
        if (!(object instanceof String))
            return null;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject((String)object);
        } catch (JSONException e) {
            Log.e(TAG, "Create json object error...", e);
            return null;
        }

        AccessToken token = new AccessToken();
        try {
            token.setAccessToken(jsonObject.getString(J_ACCESS_TOKEN));
            token.setScopes(jsonObject.getString(J_TOKEN_TYPE));
            token.setTokenType(jsonObject.getString(J_SCOPE));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return token;
    }
}
