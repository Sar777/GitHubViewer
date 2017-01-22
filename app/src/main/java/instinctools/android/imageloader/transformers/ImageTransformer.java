package instinctools.android.imageloader.transformers;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public interface ImageTransformer {
    Bitmap transform(@NonNull Bitmap bitmap);
}
