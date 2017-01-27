package instinctools.android.services.http.notification;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.constans.Constants;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.services.github.notification.GithubNotifications;
import instinctools.android.storages.SettingsStorage;
import instinctools.android.utility.Services;

public class HttpGithubAllNotificationService extends HttpGithubNotificationService {
    public HttpGithubAllNotificationService() {
        mTypeInfo = Constants.NOTIFICATION_TYPE_ALL;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Services.rescheduleAlarmBroadcast(this,
                OnAlarmReceiver.class,
                OnAlarmReceiver.REQUEST_GITHUB_NOTIFICATIONS_ALL,
                SettingsStorage.getIntervalUpdateNotifications() * 60 * 1000);

        List<Notification> notifications = GithubNotifications.getNotifications(true, false);
        if (notifications == null) {
            onHandleIntent(new ArrayList<Notification>());
            return;
        }

        onHandleIntent(notifications);
    }
}
