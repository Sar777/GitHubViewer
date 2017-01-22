package instinctools.android.services.github;

public interface GithubServiceListener<T> {
    void onError(int code);
    void onSuccess(T data);
}
