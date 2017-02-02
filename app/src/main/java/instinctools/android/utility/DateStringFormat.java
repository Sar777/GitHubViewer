package instinctools.android.utility;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DateStringFormat {
    public static final List<Long> mTimes = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));

    public static final List<String> mTimesString = Arrays.asList("year","month","day","hour","minute","second");

    public static String toDuration(long duration) {

        StringBuffer res = new StringBuffer();
        for(int i = 0; i < mTimes.size(); ++i) {
            Long current = mTimes.get(i);
            long temp = duration/current;
            if (temp > 0) {
                res.append(temp).append(" ").append(mTimesString.get(i) ).append(temp != 1 ? "s" : "").append(" ago");
                break;
            }
        }
        if("".equals(res.toString()))
            return "0 seconds ago";
        else
            return res.toString();
    }
}
