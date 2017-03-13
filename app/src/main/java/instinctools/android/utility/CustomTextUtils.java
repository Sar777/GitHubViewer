package instinctools.android.utility;

import android.text.Html;
import android.text.Spanned;

public class CustomTextUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty() || str.equals("null");

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (isEmpty(html))
            return null;

        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
