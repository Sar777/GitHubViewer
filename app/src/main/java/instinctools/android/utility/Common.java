package instinctools.android.utility;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by orion on 24.12.16.
 */

public class Common {

    public static File getDiskCacheDir(Context context) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath);
    }
}
