package instinctools.android.readers.json.transformers.github.user;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.user.User;
import instinctools.android.readers.json.transformers.ITransformer;

public class UserTransformer implements ITransformer<User> {
    private static final String TAG = "UserTransformer";

    private static final String J_LOGIN = "login";
    private static final String J_ID = "id";
    private static final String J_AVATAR_URL = "avatar_url";
    private static final String J_GRAVATAR_ID = "gravatar_id";
    private static final String J_NAME = "name";
    private static final String J_COMPANY = "company";
    private static final String J_LOCATION = "location";
    private static final String J_EMAIL = "email";
    private static final String J_TYPE = "type";
    private static final String J_PUBLIC_REPOS = "public_repos";
    private static final String J_PUBLIC_GISTS = "public_gists";
    private static final String J_PUBLIC_BIO = "bio";

    @Override
    public User transform(Object object) {
        if (!(object instanceof String))
            return null;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject((String)object);
        } catch (JSONException e) {
            Log.e(TAG, "Create json object error...", e);
            return null;
        }

        User user = new User();
        try {
            user.setId(jsonObject.getInt(J_ID));
            user.setAvatarUrl(jsonObject.getString(J_AVATAR_URL).equals("null") ? "" : jsonObject.getString(J_AVATAR_URL));
            user.setLogin(jsonObject.getString(J_LOGIN));
            user.setGravatarId(jsonObject.getString(J_GRAVATAR_ID).equals("null") ? "" : jsonObject.getString(J_GRAVATAR_ID));
            user.setName(jsonObject.getString(J_NAME).equals("null") ? "" : jsonObject.getString(J_NAME));
            user.setCompany(jsonObject.getString(J_COMPANY).equals("null") ? "" : jsonObject.getString(J_COMPANY));
            user.setLocation(jsonObject.getString(J_LOCATION).equals("null") ? "" : jsonObject.getString(J_LOCATION));
            user.setEmail(jsonObject.getString(J_EMAIL).equals("null") ? "" : jsonObject.getString(J_EMAIL));
            user.setType(jsonObject.getString(J_TYPE));
            user.setPublicRepos(jsonObject.getInt(J_PUBLIC_REPOS));
            user.setPublicGists(jsonObject.getInt(J_PUBLIC_GISTS));
            user.setBio(jsonObject.getString(J_PUBLIC_BIO).equals("null") ? "" : jsonObject.getString(J_PUBLIC_BIO));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return user;
    }
}
