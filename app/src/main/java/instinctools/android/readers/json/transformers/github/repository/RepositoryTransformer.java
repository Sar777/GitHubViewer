package instinctools.android.readers.json.transformers.github.repository;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.repositories.Repository;
import instinctools.android.readers.json.transformers.ITransformer;

class RepositoryTransformer implements ITransformer<Repository> {
    private static final String TAG = "RepositoryTransformer";

    private static final String J_ID = "id";
    private static final String J_NAME = "name";
    private static final String J_FULLNAME = "full_name";
    private static final String J_REPOSITORY_OWNER = "owner";
    private static final String J_IS_PRIVATE = "private";
    private static final String J_IS_FORK = "fork";
    private static final String J_DESCRIPTION = "description";
    private static final String J_LANGUAGE = "language";

    @Override
    public Repository transform(Object object) {
        if (!(object instanceof JSONObject))
            return null;

        JSONObject jsonObject = (JSONObject)object;

        Repository repository = new Repository();
        try {
            repository.setId(jsonObject.getInt(J_ID));
            repository.setName(jsonObject.getString(J_NAME));
            repository.setFullName(jsonObject.getString(J_FULLNAME));
            repository.setDescription(jsonObject.getString(J_DESCRIPTION).equals("null") ? "" : jsonObject.getString(J_DESCRIPTION));
            repository.setLanguage(jsonObject.getString(J_LANGUAGE).equals("null") ? "" : jsonObject.getString(J_LANGUAGE));
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
