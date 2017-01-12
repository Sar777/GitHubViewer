package instinctools.android.readers.json.transformers.books;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.Book;
import instinctools.android.readers.json.transformers.ITransformer;

/**
 * Created by orion on 2.1.17.
 */

public class BookTransformer implements ITransformer<Book, JSONObject> {
    private static final String TAG = "BookTransformer";

    private static final String J_FIELD_ID = "id";
    private static final String J_FIELD_TITLE = "title";
    private static final String J_FIELD_DESCRIPTION = "description";
    private static final String J_FIELD_IMAGE = "image_url";

    @Override
    public Book transform(JSONObject object) {
        Book book = new Book();
        try {
            book.setId(object.getInt(J_FIELD_ID));
            book.setTitle(object.getString(J_FIELD_TITLE));
            book.setDescription(object.getString(J_FIELD_DESCRIPTION));
            book.setImage(object.getString(J_FIELD_IMAGE));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return book;
    }
}