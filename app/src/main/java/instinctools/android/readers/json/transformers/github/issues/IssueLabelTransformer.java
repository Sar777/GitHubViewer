package instinctools.android.readers.json.transformers.github.issues;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.issues.IssueLabel;
import instinctools.android.readers.json.transformers.ITransformer;

public class IssueLabelTransformer implements ITransformer<IssueLabel> {
    private static final String TAG = "IssueLabelTransformer";

    private static final String J_ID = "id";
    private static final String J_URL = "url";
    private static final String J_NAME = "name";
    private static final String J_COLOR = "color";
    private static final String J_DEFAULT = "default";

    @Override
    public IssueLabel transform(Object object) {
        JSONObject jsonObject;
        if (object instanceof String) {
            try {
                jsonObject = new JSONObject((String) object);
            } catch (JSONException e) {
                Log.e(TAG, "Create json object error...", e);
                return null;
            }
        } else if (object instanceof JSONObject)
            jsonObject = (JSONObject) object;
        else
            return null;

        IssueLabel label = new IssueLabel();
        try {
            label.setId(jsonObject.getInt(J_ID));
            label.setUrl(jsonObject.getString(J_URL));
            label.setName(jsonObject.getString(J_NAME));
            label.setColor(jsonObject.getString(J_COLOR));
            label.setDefault(jsonObject.getBoolean(J_DEFAULT));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return label;
    }
}
