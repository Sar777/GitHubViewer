package instinctools.android.readers.json;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import instinctools.android.models.Book;
import instinctools.android.models.github.authorization.AuthToken;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.books.BookTransformer;
import instinctools.android.readers.json.transformers.books.BooksListTransformer;
import instinctools.android.readers.json.transformers.github.authorization.AuthTokenTransformer;
import instinctools.android.readers.json.transformers.github.repository.ListUserRepositoriesTransformer;

/**
 * Created by orion on 16.12.16.
 */

public class JsonTransformer {
    private static final String TAG = "JsonTransformer";

    private static Map<String, Class<? extends ITransformer>> mTransformersMap = new HashMap<>();

    static {
        // TODO REMOVE ME
        mTransformersMap.put(Book[].class.getName(), BooksListTransformer.class);
        mTransformersMap.put(Book.class.getName(), BookTransformer.class);

        mTransformersMap.put(AuthToken.class.getName(), AuthTokenTransformer.class);
        mTransformersMap.put(Repository[].class.getName(), ListUserRepositoriesTransformer.class);
    }

    public static <Model, T> Model transform(@NonNull String json, Class<T> clazz) {
        if (!mTransformersMap.containsKey(clazz.getName()))
            throw new UnsupportedOperationException("Not found transformer for class " + clazz.getName());

        try {
            ITransformer transformer = mTransformersMap.get(clazz.getName()).newInstance();
            return (Model) transformer.transform(json);
        } catch (Exception e) {
            Log.e(TAG, "Transform exception", e);
        }

        return null;
    }
}
