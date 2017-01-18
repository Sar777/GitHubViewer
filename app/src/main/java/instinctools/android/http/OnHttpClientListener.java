package instinctools.android.http;

/**
 * Created by orion on 12.1.17.
 */

public interface OnHttpClientListener {
    void onError(int errCode);
    void onSuccess(int code, String content);
}
