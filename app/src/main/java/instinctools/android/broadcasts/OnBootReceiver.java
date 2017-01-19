package instinctools.android.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import instinctools.android.services.HttpUpdateMyRepositoriesService;

/**
 * Created by orion on 30.12.16.
 */

public class OnBootReceiver extends BroadcastReceiver {
    private static final String TAG = "OnBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent2) {
        Intent intentService = new Intent(context, HttpUpdateMyRepositoriesService.class);
        context.startService(intentService);
    }
}
