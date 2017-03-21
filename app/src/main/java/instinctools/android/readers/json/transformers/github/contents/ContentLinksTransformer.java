package instinctools.android.readers.json.transformers.github.contents;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.contents.ContentLinks;
import instinctools.android.readers.json.transformers.ITransformer;

public class ContentLinksTransformer implements ITransformer<ContentLinks> {
    private static final String TAG = "ContentLinksTransformer";

    private static final String J_HTML = "html";
    private static final String J_GIT = "git";
    private static final String J_SELF = "self";

    @Override
    public ContentLinks transform(Object object) {
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

        ContentLinks contentLinks = new ContentLinks();
        try {
            contentLinks.setHtml(jsonObject.getString(J_HTML));
            contentLinks.setGit(jsonObject.getString(J_GIT));
            contentLinks.setSelf(jsonObject.getString(J_SELF));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return contentLinks;
    }
}
