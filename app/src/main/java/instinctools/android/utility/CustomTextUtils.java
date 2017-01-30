package instinctools.android.utility;

public class CustomTextUtils {
    public static boolean isEmpty(String str) {
        if (str == null || str.isEmpty() || str.equals("null"))
            return true;

        return false;
    }
}
