package instinctools.android.utility;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GitHubDate {
    private static final String[] TIME_FORMATS = {"yyyy/MM/dd HH:mm:ss ZZZZ","yyyy-MM-dd'T'HH:mm:ss"};

    public static Date parse(String timestamp) {
        if (TextUtils.isEmpty(timestamp))
            return null;

        for (String format : TIME_FORMATS) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(format);
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                return df.parse(timestamp);
            } catch (ParseException e) {
                // try next
            }
        }

        throw new IllegalStateException("Unable to parse the timestamp: "+ timestamp);
    }
}
