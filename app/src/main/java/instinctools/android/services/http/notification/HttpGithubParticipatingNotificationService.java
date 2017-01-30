package instinctools.android.services.http.notification;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.constans.Constants;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.services.github.notification.GithubNotifications;
import instinctools.android.storages.SettingsStorage;
import instinctools.android.utility.Services;

public class HttpGithubParticipatingNotificationService extends HttpGithubNotificationService {
    public HttpGithubParticipatingNotificationService() {
        mTypeInfo = Constants.NOTIFICATION_TYPE_PARTICIPATING;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Services.rescheduleAlarmBroadcast(this,
                OnAlarmReceiver.class,
                OnAlarmReceiver.REQUEST_GITHUB_NOTIFICATIONS_PARTICIPATING,
                SettingsStorage.getIntervalUpdateNotifications() * 60 * 1000);

        List<Notification> notifications = GithubNotifications.getNotifications(false, true);
        if (notifications == null) {
            onHandleIntent(new ArrayList<Notification>());
            return;
        }

        onHandleIntent(notifications);
    }
}
