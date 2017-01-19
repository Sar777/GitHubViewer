package instinctools.android.http;

public interface OnHttpClientListener {
    void onError(int errCode);

    void onSuccess(int code, String content);
}
