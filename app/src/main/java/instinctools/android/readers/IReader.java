package instinctools.android.readers;

/**
 * Created by orion on 16.12.16.
 */

public interface IReader<A, B> {
    B read(A data);
}
