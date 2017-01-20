package instinctools.android.readers.json;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import instinctools.android.models.github.authorization.AccessToken;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.repositories.RepositoryReadme;
import instinctools.android.models.github.user.User;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.authorization.AccessTokenTransformer;
import instinctools.android.readers.json.transformers.github.errors.ErrorResponseTransformers;
import instinctools.android.readers.json.transformers.github.repository.ListUserRepositoriesTransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryReadmeTransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryTransformer;
import instinctools.android.readers.json.transformers.github.user.UserTransformer;

public class JsonTransformer {
    private static final String TAG = "JsonTransformer";

    private static Map<String, Class<? extends ITransformer>> mTransformersMap = new HashMap<>();

    static {
        mTransformersMap.put(User.class.getName(), UserTransformer.class);
        mTransformersMap.put(AccessToken.class.getName(), AccessTokenTransformer.class);
        mTransformersMap.put(Repository.class.getName(), RepositoryTransformer.class);
        mTransformersMap.put(Repository[].class.getName(), ListUserRepositoriesTransformer.class);
        mTransformersMap.put(RepositoryReadme.class.getName(), RepositoryReadmeTransformer.class);
        mTransformersMap.put(ErrorResponse.class.getName(), ErrorResponseTransformers.class);
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
