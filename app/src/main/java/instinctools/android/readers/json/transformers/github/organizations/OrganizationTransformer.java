package instinctools.android.readers.json.transformers.github.organizations;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.organizations.Organization;
import instinctools.android.readers.json.transformers.ITransformer;

public class OrganizationTransformer implements ITransformer<Organization> {
    private static final String TAG = "OrganizationTransformer";

    private static final String J_LOGIN = "login";
    private static final String J_ID = "id";
    private static final String J_URL = "url";
    private static final String J_REPOS_URL = "repos_url";
    private static final String J_EVENTS_URL = "events_url";
    private static final String J_HOOKS_URL = "hooks_url";
    private static final String J_ISSUES_URL = "issues_url";
    private static final String J_MEMBERS_URL = "members_url";
    private static final String J_PUBLIC_MEMBERS_URL = "public_members_url";
    private static final String J_AVATAR_URL = "avatar_url";
    private static final String J_DESCRIPTION = "description";

    @Override
    public Organization transform(Object object) {
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

        Organization organization = new Organization();
        try {
            organization.setId(jsonObject.getInt(J_ID));
            organization.setLogin(jsonObject.getString(J_LOGIN));
            organization.setUrl(jsonObject.getString(J_URL));
            organization.setReposUrl(jsonObject.getString(J_REPOS_URL));
            organization.setEventsUrl(jsonObject.getString(J_EVENTS_URL));
            organization.setHooksUrl(jsonObject.getString(J_HOOKS_URL));
            organization.setIssuesUrl(jsonObject.getString(J_ISSUES_URL));
            organization.setMembersUrl(jsonObject.getString(J_MEMBERS_URL));
            organization.setPublicMembersUrl(jsonObject.getString(J_PUBLIC_MEMBERS_URL));
            organization.setAvatarUrl(jsonObject.getString(J_AVATAR_URL));
            organization.setDescription(jsonObject.getString(J_DESCRIPTION));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return organization;
    }
}
