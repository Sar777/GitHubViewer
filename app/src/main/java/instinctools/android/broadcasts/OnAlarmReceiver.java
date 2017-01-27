package instinctools.android.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import instinctools.android.services.http.notification.HttpGithubAllNotificationService;
import instinctools.android.services.http.notification.HttpGithubParticipatingNotificationService;
import instinctools.android.services.http.notification.HttpGithubUnreadNotificationService;
import instinctools.android.services.http.repository.HttpUpdateMyRepositoriesService;
import instinctools.android.services.http.repository.HttpUpdateStarsRepositoriesService;
import instinctools.android.services.http.repository.HttpUpdateWatchRepositoriesService;

public class OnAlarmReceiver extends BroadcastReceiver {
    public static final String REQUEST_CODE_FIELD = "REQUEST_CODE";

    // Repositories
    public static final int REQUEST_MY_REPO_CODE = 1;
    public static final int REQUEST_WATCH_REPO_CODE = 2;
    public static final int REQUEST_STARS_REPO_CODE = 3;
    // Notifications
    public static final int REQUEST_GITHUB_NOTIFICATIONS_UNREAD = 4;
    public static final int REQUEST_GITHUB_NOTIFICATIONS_ALL = 5;
    public static final int REQUEST_GITHUB_NOTIFICATIONS_PARTICIPATING = 6;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService;
        switch (intent.getIntExtra(REQUEST_CODE_FIELD, -1)) {
            // Repository
            case REQUEST_MY_REPO_CODE:
                intentService = new Intent(context, HttpUpdateMyRepositoriesService.class);
                break;
            case REQUEST_WATCH_REPO_CODE:
                intentService = new Intent(context, HttpUpdateWatchRepositoriesService.class);
                break;
            case REQUEST_STARS_REPO_CODE:
                intentService = new Intent(context, HttpUpdateStarsRepositoriesService.class);
                break;
            // Notification
            case REQUEST_GITHUB_NOTIFICATIONS_ALL:
                intentService = new Intent(context, HttpGithubAllNotificationService.class);
                break;
            case REQUEST_GITHUB_NOTIFICATIONS_PARTICIPATING:
                intentService = new Intent(context, HttpGithubParticipatingNotificationService.class);
                break;
            case REQUEST_GITHUB_NOTIFICATIONS_UNREAD:
                intentService = new Intent(context, HttpGithubUnreadNotificationService.class);
                break;
            default:
                return;
        }

        context.startService(intentService);
    }
}