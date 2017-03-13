package instinctools.android.services.github;

import android.support.annotation.Nullable;

import instinctools.android.models.github.errors.ErrorResponse;

public interface GithubServiceListener<T> {
    void onError(int code, @Nullable ErrorResponse response);
    void onSuccess(T data);
}
