package instinctools.android.readers.json.transformers.github.events.payload.release;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.events.payload.release.PayloadAsset;
import instinctools.android.readers.json.transformers.ITransformer;

public class ListEventPayloadReleaseAssetTransformer implements ITransformer<List<PayloadAsset>> {
    private static final String TAG = "ListEventPayAssTrans";

    @Override
    public List<PayloadAsset> transform(Object object) {
        JSONArray jsonArray;
        if (object instanceof String) {
            try {
                jsonArray = new JSONArray((String) object);
            } catch (JSONException e) {
                Log.e(TAG, "Create json object error...", e);
                return null;
            }
        } else if (object instanceof JSONArray)
            jsonArray = (JSONArray) object;
        else
            return new ArrayList<>();

        List<PayloadAsset> payloadAssets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                payloadAssets.add(new EventPayloadReleaseAssetTransformer().transform(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json object fail", e);
            }
        }

        return payloadAssets;
    }
}
