package instinctools.android.imageloader;

import android.graphics.Bitmap;

public interface ImageLoadingStateListener {
    void onPrepare();

    void onError();

    void onLoaded(Bitmap bitmap);
}
