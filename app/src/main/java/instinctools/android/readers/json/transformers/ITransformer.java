package instinctools.android.readers.json.transformers;

public interface ITransformer<T> {
    T transform(Object object);
}
