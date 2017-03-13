package instinctools.android.readers.json.transformers.github.repository;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.repositories.RepositoryReadme;
import instinctools.android.readers.json.transformers.ITransformer;

public class RepositoryReadmeTransformer implements ITransformer<RepositoryReadme> {
    private static final String TAG = "RepReadmeTransform";

    private static final String J_TYPE = "type";
    private static final String J_ENCODING = "encoding";
    private static final String J_SIZE = "size";
    private static final String J_NAME = "name";
    private static final String J_PATH = "path";
    private static final String J_CONTENT = "content";
    private static final String J_SHA = "sha";
    private static final String J_URL = "url";
    private static final String J_GIT_URL = "git_url";
    private static final String J_HTML_URL = "html_url";
    private static final String J_DOWNLOAD_URL = "download_url";
    private static final String J_LINKS = "_links";

    @Override
    public RepositoryReadme transform(Object object) {
        if (!(object instanceof String))
            return null;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject((String)object);
        } catch (JSONException e) {
            Log.e(TAG, "Create json object error...", e);
            return null;
        }

        RepositoryReadme repositoryReadme = new RepositoryReadme();
        try {
            repositoryReadme.setType(jsonObject.getString(J_TYPE));
            repositoryReadme.setEncoding(jsonObject.getString(J_ENCODING));
            repositoryReadme.setSize(jsonObject.getInt(J_SIZE));
            repositoryReadme.setName(jsonObject.getString(J_NAME));
            repositoryReadme.setPath(jsonObject.getString(J_PATH));
            repositoryReadme.setContent(jsonObject.getString(J_CONTENT));
            repositoryReadme.setSha(jsonObject.getString(J_SHA));
            repositoryReadme.setUrl(jsonObject.getString(J_URL));
            repositoryReadme.setGitUrl(jsonObject.getString(J_GIT_URL));
            repositoryReadme.setHtmlUrl(jsonObject.getString(J_HTML_URL));
            repositoryReadme.setDownloadUrl(jsonObject.getString(J_DOWNLOAD_URL));
            repositoryReadme.setLinks(new RepositoryReadmeLinksTransformer().transform(jsonObject.getString(J_LINKS)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return repositoryReadme;
    }
}
