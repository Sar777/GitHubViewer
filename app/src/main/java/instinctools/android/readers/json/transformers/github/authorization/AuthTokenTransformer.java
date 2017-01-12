package instinctools.android.readers.json.transformers.github.authorization;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import instinctools.android.models.github.authorization.AuthToken;
import instinctools.android.readers.json.transformers.ITransformer;

/**
 * Created by orion on 12.1.17.
 */

public class AuthTokenTransformer implements ITransformer<AuthToken> {
    private static final String TAG = "AuthTokenTransformer";

    private static final String J_ID = "id";
    private static final String J_URL = "url";
    private static final String J_SCOPES = "scopes";
    private static final String J_TOKEN = "token";
    private static final String J_TOKEN_LAST_EIGHT= "token_last_eight";
    private static final String J_HASHED_TOKEN = "hashed_token";
    private static final String J_APP = "app";
    private static final String J_NOTE = "note";
    private static final String J_NOTE_URL = "note_url";
    private static final String J_UPDATE_AT = "updated_at";
    private static final String J_CREATED_AT = "created_at";
    private static final String J_FINGERPRINT = "fingerprint";

    @Override
    public AuthToken transform(Object object) {
        if (!(object instanceof String))
            return null;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject((String)object);
        } catch (JSONException e) {
            Log.e(TAG, "Create json object error...", e);
            return null;
        }

        AuthToken token = new AuthToken();
        try {
            token.setId(jsonObject.getInt(J_ID));
            token.setUrl(jsonObject.getString(J_URL));

            Set<String> scopes = new HashSet<>();
            for (int i = 0; i < jsonObject.getJSONArray(J_SCOPES).length(); ++i)
                scopes.add(jsonObject.getJSONArray(J_SCOPES).getString(i));
            token.setScopes(scopes);

            token.setToken(jsonObject.getString(J_TOKEN));
            token.setTokenLastEight(jsonObject.getString(J_TOKEN_LAST_EIGHT));
            token.setHashedToken(jsonObject.getString(J_HASHED_TOKEN));

            token.setAppToken(new AppTokenTransformer().transform(jsonObject.getJSONObject(J_APP)));

            token.setNote(jsonObject.getString(J_NOTE));
            token.setNoteUrl(jsonObject.getString(J_NOTE_URL));
            token.setUpdatedAt(jsonObject.getString(J_UPDATE_AT));
            token.setCreatedAt(jsonObject.getString(J_CREATED_AT));
            token.setFingerprint(jsonObject.getString(J_FINGERPRINT));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return token;
    }
}
