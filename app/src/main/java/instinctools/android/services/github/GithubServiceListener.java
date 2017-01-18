package instinctools.android.services.github;

/**
 * Created by orion on 13.1.17.
 */

public interface GithubServiceListener<T> {
    void onError(int code);
    void onSuccess(T data);
}
