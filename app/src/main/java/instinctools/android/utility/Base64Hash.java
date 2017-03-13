package instinctools.android.utility;

import android.support.annotation.NonNull;
import android.util.Base64;

public class Base64Hash {
    public static String create(@NonNull String data) {
        return Base64.encodeToString(data.getBytes(), Base64.NO_WRAP);
    }
}
