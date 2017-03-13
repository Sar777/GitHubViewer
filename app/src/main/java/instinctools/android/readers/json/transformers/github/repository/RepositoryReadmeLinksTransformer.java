package instinctools.android.readers.json.transformers.github.repository;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.repositories.RepositoryReadmeLinks;
import instinctools.android.readers.json.transformers.ITransformer;

public class RepositoryReadmeLinksTransformer implements ITransformer<RepositoryReadmeLinks> {
    private static final String TAG = "RepReadLinksTransformer";

    private static final String J_GIT = "git";
    private static final String J_SELF = "self";
    private static final String J_HTML = "html";

    @Override
    public RepositoryReadmeLinks transform(Object object) {
        if (!(object instanceof JSONObject))
            return null;

        JSONObject jsonObject = (JSONObject) object;

        RepositoryReadmeLinks repositoryReadmeLinks = new RepositoryReadmeLinks();
        try {
            repositoryReadmeLinks.setGit(jsonObject.getString(J_GIT));
            repositoryReadmeLinks.setSelf(jsonObject.getString(J_SELF));
            repositoryReadmeLinks.setHtml(jsonObject.getString(J_HTML));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return repositoryReadmeLinks;
    }
}
