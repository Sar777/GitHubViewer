package instinctools.android.readers.json.transformers.github.contents;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.contents.Content;
import instinctools.android.models.github.contents.ContentType;
import instinctools.android.readers.json.transformers.ITransformer;

public class ContentTransformer implements ITransformer<Content> {
    private static final String TAG = "ContentTransformer";

    private static final String J_NAME = "name";
    private static final String J_PATH = "path";
    private static final String J_SHA = "sha";
    private static final String J_SIZE = "size";
    private static final String J_URL = "url";
    private static final String J_HTML_URL = "html_url";
    private static final String J_TYPE = "type";
    private static final String J_LINKS = "_links";

    @Override
    public Content transform(Object object) {
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

        Content content = new Content();
        try {
            content.setName(jsonObject.getString(J_NAME));
            content.setPath(jsonObject.getString(J_PATH));
            content.setSha(jsonObject.getString(J_SHA));
            content.setSize(jsonObject.getInt(J_SIZE));
            content.setUrl(jsonObject.getString(J_URL));
            content.setHtmlUrl(jsonObject.getString(J_HTML_URL));
            content.setType(ContentType.get(jsonObject.getString(J_TYPE)));
            content.setLinks(new ContentLinksTransformer().transform(jsonObject.getJSONObject(J_LINKS)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return content;
    }
}
