package instinctools.android.services.http.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

import instinctools.android.R;
import instinctools.android.activity.NotificationActivity;
import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.constans.Constants;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.models.github.notification.NotificationListResponse;
import instinctools.android.services.github.notification.GithubServiceNotifications;
import instinctools.android.storages.SettingsStorage;
import instinctools.android.utility.Services;

public class HttpGithubUnreadNotificationService extends HttpGithubNotificationService {
    public HttpGithubUnreadNotificationService() {
        mTypeInfo = Constants.NOTIFICATION_TYPE_UNREAD;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Services.rescheduleAlarmBroadcast(this,
                OnAlarmReceiver.class,
                OnAlarmReceiver.REQUEST_GITHUB_NOTIFICATIONS_UNREAD,
                SettingsStorage.getIntervalUpdateNotifications() * 60 * 1000);

        NotificationListResponse response = GithubServiceNotifications.getNotificationsResponse(false, false);
        if (response == null || response.getNotifications().size() == 0) {
            onHandleIntent(new ArrayList<Notification>());
            return;
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_github_bell)
                        .setContentText(response.getNotifications().get(0).getSubject().getTitle())
                        .setContentTitle(getString(R.string.msg_github_notifications));

        Intent notifyIntent = new Intent(this, NotificationActivity.class);

        if (response.getNotifications().size() > 1)
            builder.setNumber(response.getNotifications().size());

        /// TODO not supported
        //if (response.getNotifications().size() == 1)
        //    notifyIntent.putExtra(NotificationActivity.NOTIFICATION_URL, response.getNotifications().get(0).getUrl());

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(notifyPendingIntent);
        notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());

        onHandleIntent(response.getNotifications());
    }
}
