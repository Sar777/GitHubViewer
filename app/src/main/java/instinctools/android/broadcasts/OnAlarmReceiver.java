package instinctools.android.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import instinctools.android.services.HttpUpdateMyRepositoriesService;
import instinctools.android.services.HttpUpdateStarsRepositoriesService;
import instinctools.android.services.HttpUpdateWatchRepositoriesService;

public class OnAlarmReceiver extends BroadcastReceiver {
    public static final String REQUEST_CODE_FIELD = "REQUEST_CODE";

    public static final int REQUEST_MY_REPO_CODE = 1;
    public static final int REQUEST_WATCH_REPO_CODE = 2;
    public static final int REQUEST_STARS_REPO_CODE = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService;
        switch (intent.getIntExtra(REQUEST_CODE_FIELD, -1)) {
            case REQUEST_MY_REPO_CODE:
                intentService = new Intent(context, HttpUpdateMyRepositoriesService.class);
                break;
            case REQUEST_WATCH_REPO_CODE:
                intentService = new Intent(context, HttpUpdateWatchRepositoriesService.class);
                break;
            case REQUEST_STARS_REPO_CODE:
                intentService = new Intent(context, HttpUpdateStarsRepositoriesService.class);
                break;
            default:
                return;
        }

        context.startService(intentService);
    }
}
