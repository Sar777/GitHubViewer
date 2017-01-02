package instinctools.android.readers.json.transformers.books;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.data.Book;
import instinctools.android.readers.json.transformers.ITransformer;

/**
 * Created by orion on 2.1.17.
 */

public class BooksListTransformer implements ITransformer<List<Book>, JSONObject> {
    private static final String TAG = "BooksListTransformer";

    private static final String J_CONTAINER = "data";

    @Override
    public List<Book> transform(JSONObject object) {
        JSONArray dataArray = null;
        try {
            dataArray = object.getJSONArray(J_CONTAINER);
        } catch (JSONException e) {
            Log.e(TAG, "Create json array object error...", e);
        }

        List<Book> bookList = new ArrayList<>(dataArray.length());
        for (int i = 0; i < dataArray.length(); ++i)
            try {
                bookList.add(new BookTransformer().transform(dataArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json object fail", e);
            }

        return bookList;
    }
}