package instinctools.android.readers.json.transformers.github.user;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.user.User;
import instinctools.android.readers.json.transformers.ITransformer;

public class ListUsersTransformer implements ITransformer<List<User>> {
    private static final String TAG = "ListUserRepositories";

    @Override
    public List<User> transform(Object object) {
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

        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i)
            try {
                users.add(new UserTransformer().transform(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json object fail", e);
            }

        return users;
    }
}
