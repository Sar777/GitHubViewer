package instinctools.android.utility;

/**
 * Created by orion on 20.1.17.
 */

public class CustomTextUtils {
    public static boolean isEmpty(String str) {
        if (str == null || str.isEmpty() || str.equals("null"))
            return true;

        return false;
    }
}
