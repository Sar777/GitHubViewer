package instinctools.android.http;

public interface OnHttpClientListener {
    void onError(int code, String content);

    void onSuccess(int code, String content);
}
