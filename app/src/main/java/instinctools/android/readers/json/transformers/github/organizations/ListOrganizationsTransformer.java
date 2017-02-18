package instinctools.android.readers.json.transformers.github.organizations;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.organizations.Organization;
import instinctools.android.readers.json.transformers.ITransformer;

public class ListOrganizationsTransformer implements ITransformer<List<Organization>> {
    private static final String TAG = "ListOrganizationsTrans";

    @Override
    public List<Organization> transform(Object object) {
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

        List<Organization> organizations = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i)
            try {
                organizations.add(new OrganizationTransformer().transform(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json object fail", e);
            }

        return organizations;
    }
}
