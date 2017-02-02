package instinctools.android.readers.json.transformers.github.user;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.user.User;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.utility.CustomTextUtils;

public class UserTransformer implements ITransformer<User> {
    private static final String TAG = "SearchUserTransformer";

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
    private static final String J_FOLLOWING = "following";
    private static final String J_FOLLOWERS = "followers";
    private static final String J_BIO = "bio";
    private static final String J_BLOG = "blog";

    @Override
    public User transform(Object object) {
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

        User user = new User();
        try {
            user.setId(jsonObject.getInt(J_ID));
            user.setAvatarUrl(CustomTextUtils.isEmpty(jsonObject.getString(J_AVATAR_URL)) ? "" : jsonObject.getString(J_AVATAR_URL));
            user.setLogin(jsonObject.getString(J_LOGIN));
            user.setGravatarId(CustomTextUtils.isEmpty(jsonObject.getString(J_GRAVATAR_ID)) ? "" : jsonObject.getString(J_GRAVATAR_ID));
            user.setName(CustomTextUtils.isEmpty(jsonObject.getString(J_NAME)) ? "None" : jsonObject.getString(J_NAME));
            user.setCompany(CustomTextUtils.isEmpty(jsonObject.getString(J_COMPANY)) ? "None" : jsonObject.getString(J_COMPANY));
            user.setLocation(CustomTextUtils.isEmpty(jsonObject.getString(J_LOCATION)) ? "None" : jsonObject.getString(J_LOCATION));
            user.setEmail(CustomTextUtils.isEmpty(jsonObject.getString(J_EMAIL)) ? "None" : jsonObject.getString(J_EMAIL));
            user.setType(jsonObject.getString(J_TYPE));
            user.setPublicRepos(jsonObject.getInt(J_PUBLIC_REPOS));
            user.setPublicGists(jsonObject.getInt(J_PUBLIC_GISTS));
            user.setFollowers(jsonObject.getString(J_FOLLOWERS));
            user.setFollowing(jsonObject.getString(J_FOLLOWING));
            user.setBio(CustomTextUtils.isEmpty(jsonObject.getString(J_BIO)) ? "None" : jsonObject.getString(J_BIO));
            user.setBlog(CustomTextUtils.isEmpty(jsonObject.getString(J_BLOG)) ? "None" : jsonObject.getString(J_BLOG));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return user;
    }
}
