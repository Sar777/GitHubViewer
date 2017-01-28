package instinctools.android.readers.json.transformers.github.errors;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.readers.json.transformers.ITransformer;

public class ErrorResponseTransformer implements ITransformer<ErrorResponse> {
    private static final String TAG = "ErrorRespTransform";

    private static final String J_MESSAGE = "message";
    private static final String J_DOCUMENTATION_URL = "documentation_url";

    @Override
    public ErrorResponse transform(Object object) {
        if (object == null)
            return null;

        if (!(object instanceof String))
            return null;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject((String)object);
        } catch (JSONException e) {
            Log.e(TAG, "Create json object error...", e);
            return null;
        }

        ErrorResponse response = new ErrorResponse();
        try {
            response.setMessage(jsonObject.getString(J_MESSAGE));
            response.setDocumentationUrl(jsonObject.getString(J_DOCUMENTATION_URL));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return response;
    }
}
