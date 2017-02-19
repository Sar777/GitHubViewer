package instinctools.android.readers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.Book;

/**
 * Created by orion on 16.12.16.
 */

public class JsonBookReader implements IReader<String, List<Book>> {
    private static final String TAG = "JsonBookReader";

    private static final String J_CONTAINER = "data";
    private static final String J_FIELD_TITLE = "title";
    private static final String J_FIELD_DESCRIPTION = "description";
    private static final String J_FIELD_IMAGE = "image_url";

    @Override
    public List<Book> read(String json) {

        List<Book> objects = new ArrayList<>();
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "Create json object error...", e);
        }

        if (obj == null)
            return objects;

        JSONArray dataArray = null;
        try {
            dataArray = obj.getJSONArray(J_CONTAINER);
        } catch (JSONException e) {
            Log.e(TAG, "Create json array object error...", e);
        }

        if (dataArray == null)
            return objects;

        for (int i = 0; i < dataArray.length(); ++i) {
            Book book = new Book();
            try {
                book.setTitle(dataArray.getJSONObject(i).getString(J_FIELD_TITLE));
                book.setDescription(dataArray.getJSONObject(i).getString(J_FIELD_DESCRIPTION));
                book.setImage(dataArray.getJSONObject(i).getString(J_FIELD_IMAGE));
                objects.add(book);
            } catch (JSONException e) {
                Log.e(TAG, "Json field error...", e);
            }
        }

        return objects;
    }
}
