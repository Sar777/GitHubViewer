package instinctools.android.readers.json;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import instinctools.android.models.Book;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.books.BookTransformer;
import instinctools.android.readers.json.transformers.books.BooksListTransformer;

/**
 * Created by orion on 16.12.16.
 */

public class JsonTransformer {
    private static final String TAG = "JsonTransformer";

    private static Map<String, Class<? extends ITransformer>> mTransformersMap = new HashMap<>();

    static {
        mTransformersMap.put(Book[].class.getName(), BooksListTransformer.class);
        mTransformersMap.put(Book.class.getName(), BookTransformer.class);
    }

    public static <Model, T> Model transform(String json, Class<T> clazz) {
        if (!mTransformersMap.containsKey(clazz.getName()))
            throw new UnsupportedOperationException("Not found transformer for class " + clazz.getName());

        JSONObject obj;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "Create json object error...", e);
            return null;
        }

        try {
            ITransformer transformer = mTransformersMap.get(clazz.getName()).newInstance();
            return (Model) transformer.transform(obj);
        } catch (Exception e) {
            Log.e(TAG, "Transform exception", e);
        }

        return null;
    }
}
