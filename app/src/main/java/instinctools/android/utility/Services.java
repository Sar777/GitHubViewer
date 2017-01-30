package instinctools.android.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import instinctools.android.broadcasts.OnAlarmReceiver;

public class Services {
    public static void stopAlarmBroadcast(Context context, Class<? extends BroadcastReceiver> clazz, int id) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, new Intent(context, clazz), PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
    }

    public static void rescheduleAlarmBroadcast(Context context, Class<? extends BroadcastReceiver> clazz, int id, int interval) {
        stopAlarmBroadcast(context, clazz, id);

        Intent alarmIntent = new Intent(context, clazz);
        alarmIntent.putExtra(OnAlarmReceiver.REQUEST_CODE_FIELD, id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarm.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
        else
            alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
    }
}
