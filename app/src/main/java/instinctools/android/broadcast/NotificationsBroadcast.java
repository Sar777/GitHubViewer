package instinctools.android.broadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.activity.NotificationActivity;
import instinctools.android.constans.Constants;
import instinctools.android.models.github.notification.Notification;

public class NotificationsBroadcast extends BroadcastReceiver {
    public static final String INTENT_EXTRA_NOTIFICATIONS = "NOTIFICATIONS";

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Notification> notifications = intent.getParcelableArrayListExtra(INTENT_EXTRA_NOTIFICATIONS);
        if (notifications.isEmpty())
            return;

        if (App.isNotificationActivityVisible())
            return;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_github_bell)
                        .setAutoCancel(true)
                        .setContentText(notifications.get(0).getSubject().getTitle());

        if (notifications.size() > 1) {
            builder.setNumber(notifications.size());
            builder.setContentTitle(String.format(context.getString(R.string.msg_github_notifications_stack), notifications.size()));
        }
        else
            builder.setContentTitle(context.getString(R.string.msg_github_notifications));

        Intent notifyIntent = new Intent(context, NotificationActivity.class);

        /// TODO not supported
        //if (notifications.size() == 1)
        //    notifyIntent.putExtra(NotificationActivity.NOTIFICATION_URL, response.getNotifications().get(0).getUrl());

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(notifyPendingIntent);
        notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());
    }
}
