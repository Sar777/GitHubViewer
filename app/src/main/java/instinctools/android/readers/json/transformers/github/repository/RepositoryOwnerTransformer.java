package instinctools.android.readers.json.transformers.github.repository;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.repositories.RepositoryOwner;
import instinctools.android.readers.json.transformers.ITransformer;

public class RepositoryOwnerTransformer implements ITransformer<RepositoryOwner> {
    private static final String TAG = "RepositoryOwnerTrans";

    private static final String J_LOGIN = "login";
    private static final String J_ID = "id";
    private static final String J_AVATAR_URL = "avatar_url";

    @Override
    public RepositoryOwner transform(Object object) {
        if (!(object instanceof JSONObject))
            return null;

        JSONObject jsonObject = (JSONObject)object;

        RepositoryOwner owner = new RepositoryOwner();
        try {
            owner.setId(jsonObject.getInt(J_ID));
            owner.setLogin(jsonObject.getString(J_LOGIN));
            owner.setAvatarUrl(jsonObject.getString(J_AVATAR_URL));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return owner;
    }
}
