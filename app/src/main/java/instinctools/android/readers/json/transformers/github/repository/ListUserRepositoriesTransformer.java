package instinctools.android.readers.json.transformers.github.repository;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.repositories.Repository;
import instinctools.android.readers.json.transformers.ITransformer;

public class ListUserRepositoriesTransformer implements ITransformer<List<Repository>> {
    private static final String TAG = "ListUserRepositories";

    @Override
    public List<Repository> transform(Object object) {
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

        List<Repository> bookList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i)
            try {
                bookList.add(new RepositoryTransformer().transform(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json object fail", e);
            }

        return bookList;
    }
}
