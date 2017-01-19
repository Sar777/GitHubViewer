package instinctools.android.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import instinctools.android.broadcasts.OnAlarmReceiver;

public class HttpRunAllService extends IntentService {
    private static final String TAG = "HttpRunAllService";

    private static final long INTERVAL_ALARM_PENDING = 10 * 60 * 1000; // 10 min

    public HttpRunAllService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // My Repositories
        startService(new Intent(this, HttpUpdateMyRepositoriesService.class));
        // My watch repositories
        startService(new Intent(this, HttpUpdateWatchRepositoryService.class));
        // My starts repositories
        startService(new Intent(this, HttpUpdateStarsRepositoryService.class));

        Intent alarmIntent = new Intent(this, OnAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, OnAlarmReceiver.REQUEST_ALARM_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarm.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVAL_ALARM_PENDING, pIntent);
        else
            alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVAL_ALARM_PENDING, pIntent);
    }
}
