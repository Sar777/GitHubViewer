package instinctools.android.readers.json;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import instinctools.android.data.Book;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.books.BooksListTransformer;

/**
 * Created by orion on 16.12.16.
 */

public class JsonTransformer {
    private static final String TAG = "JsonTransformer";

    private static Map<Class, Class<? extends ITransformer>> transformerMap = new HashMap<>();

    static {
        transformerMap.put(Book.class, BooksListTransformer.class);
    }

    public static <Model> Model transform(String json, Class<?> clazz) {
        if (!transformerMap.containsKey(clazz))
            throw new UnsupportedOperationException("Not found transformer for class " + clazz.getName());

        JSONObject obj;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "Create json object error...", e);
            return null;
        }

        try {
            ITransformer transformer = transformerMap.get(clazz).newInstance();
            return (Model) transformer.transform(obj);
        } catch (Exception e) {
            Log.e(TAG, "Transform exception", e);
        }

        return null;
    }
}
