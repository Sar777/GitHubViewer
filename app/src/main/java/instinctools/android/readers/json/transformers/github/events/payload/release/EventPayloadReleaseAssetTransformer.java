package instinctools.android.readers.json.transformers.github.events.payload.release;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.events.payload.release.PayloadAsset;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.events.EventAuthorTransformer;

class EventPayloadReleaseAssetTransformer implements ITransformer<PayloadAsset> {
    private static final String TAG = "EventPayloadAssetTrans";

    private static final String J_ID = "id";
    private static final String J_URL = "url";
    private static final String J_NAME = "name";
    private static final String J_LABEL = "label";
    private static final String J_UPLOADER = "uploader";
    private static final String J_SIZE = "size";
    private static final String J_DOWNLOAD_COUNT = "download_count";
    private static final String J_BROWSER_DOWNLOAD_URL = "browser_download_url";

    @Override
    public PayloadAsset transform(Object object) {
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

        PayloadAsset payloadAsset = new PayloadAsset();
        try {
            payloadAsset.setId(jsonObject.getInt(J_ID));
            payloadAsset.setUrl(jsonObject.getString(J_URL));
            payloadAsset.setName(jsonObject.getString(J_NAME));
            payloadAsset.setLabel(jsonObject.getString(J_LABEL));
            payloadAsset.setUploader(new EventAuthorTransformer().transform(jsonObject.getJSONObject(J_UPLOADER)));
            payloadAsset.setSize(jsonObject.getInt(J_SIZE));
            payloadAsset.setDownloadCount(jsonObject.getInt(J_DOWNLOAD_COUNT));
            payloadAsset.setBrowserDownloadUrl(jsonObject.getString(J_BROWSER_DOWNLOAD_URL));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return payloadAsset;
    }
}
