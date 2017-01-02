package instinctools.android.broadcasts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by orion on 30.12.16.
 */

public class OnBootReceiver extends BroadcastReceiver {
    private static final String TAG = "OnBootReceiver";

    private static final long INTERVAL_ALARM_PENDING = 10 * 60 * 1000; // 10 min

    @Override
    public void onReceive(Context context, Intent intent2) {
        Intent alarmIntent = new Intent(context.getApplicationContext(), OnAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context.getApplicationContext(), OnAlarmReceiver.REQUEST_ALARM_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL_ALARM_PENDING, pIntent);
    }
}
