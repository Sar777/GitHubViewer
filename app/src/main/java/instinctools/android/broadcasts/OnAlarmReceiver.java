package instinctools.android.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import instinctools.android.services.HttpUpdateDataService;

/**
 * Created by orion on 30.12.16.
 */

public class OnAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "OnAlarmReceiver";
    public static final int REQUEST_ALARM_CODE = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, HttpUpdateDataService.class);
        context.startService(intentService);
    }
}
