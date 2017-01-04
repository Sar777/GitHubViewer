package instinctools.android.imageloader;

import android.graphics.Bitmap;

/**
 * Created by orion on 29.12.16.
 */

public interface ImageLoadingStateListener {
    void onPrepare();

    void onError();

    void onLoaded(Bitmap bitmap);
}
