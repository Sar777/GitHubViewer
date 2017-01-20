package instinctools.android.readers.json.transformers.github.repository;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.repositories.Repository;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.utility.CustomTextUtils;

public class RepositoryTransformer implements ITransformer<Repository> {
    private static final String TAG = "RepositoryTransformer";

    private static final String J_ID = "id";
    private static final String J_NAME = "name";
    private static final String J_HTML_URL = "html_url";
    private static final String J_FULLNAME = "full_name";
    private static final String J_DEFAULT_BRANCH = "default_branch";
    private static final String J_REPOSITORY_OWNER = "owner";
    private static final String J_IS_PRIVATE = "private";
    private static final String J_IS_FORK = "fork";
    private static final String J_DESCRIPTION = "description";
    private static final String J_LANGUAGE = "language";
    private static final String J_FORKS = "forks_count";
    private static final String J_STARGAZERS = "stargazers_count";
    private static final String J_WATCHERS = "watchers_count";
    private static final String J_OPEN_ISSUES = "open_issues_count";

    @Override
    public Repository transform(Object object) {
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

        Repository repository = new Repository();
        try {
            repository.setId(jsonObject.getInt(J_ID));
            repository.setName(jsonObject.getString(J_NAME));
            repository.setHtmlUrl(jsonObject.getString(J_HTML_URL));
            repository.setFullName(jsonObject.getString(J_FULLNAME));
            repository.setDescription(CustomTextUtils.isEmpty(jsonObject.getString(J_DESCRIPTION)) ? "Empty" : jsonObject.getString(J_DESCRIPTION));
            repository.setDefaultBranch(jsonObject.getString(J_DEFAULT_BRANCH));
            repository.setLanguage(CustomTextUtils.isEmpty(jsonObject.getString(J_LANGUAGE)) ? "None" : jsonObject.getString(J_LANGUAGE));
            repository.setForks(jsonObject.getInt(J_FORKS));
            repository.setStargazers(jsonObject.getInt(J_STARGAZERS));
            repository.setWatchers(jsonObject.getInt(J_WATCHERS));
            repository.setOpenIssues(jsonObject.getInt(J_OPEN_ISSUES));
            repository.setIsPrivate(jsonObject.getBoolean(J_IS_PRIVATE));
            repository.setIsFork(jsonObject.getBoolean(J_IS_FORK));
            repository.setRepositoryOwner(new RepositoryOwnerTransformer().transform(jsonObject.getJSONObject(J_REPOSITORY_OWNER)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return repository;
    }
}
