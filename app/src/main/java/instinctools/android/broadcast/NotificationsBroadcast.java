package instinctools.android.broadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Iterator;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.activity.NotificationActivity;
import instinctools.android.constans.Constants;
import instinctools.android.database.providers.NotificationsProvider;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.storages.SettingsStorage;

public class NotificationsBroadcast extends BroadcastReceiver {
    public static final String INTENT_EXTRA_NOTIFICATIONS = "NOTIFICATIONS";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (App.isNotificationActivityVisible())
            return;

        ArrayList<Notification> notifications = intent.getParcelableArrayListExtra(INTENT_EXTRA_NOTIFICATIONS);

        Cursor cursor = context.getContentResolver().query(NotificationsProvider.NOTIFICATIONS_CONTENT_URI, null, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                Notification cursorNotification = Notification.fromCursor(cursor);
                Iterator itr = notifications.iterator();
                while (itr.hasNext()) {
                    if (itr.next().equals(cursorNotification))
                        itr.remove();
                }
            }

            cursor.close();
        }

        if (notifications.isEmpty())
            return;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_github_bell)
                        .setAutoCancel(true);

        if (SettingsStorage.isNotificationSound())
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        if (notifications.size() > 1) {
            builder.setNumber(notifications.size());
            builder.setContentTitle(String.format(context.getString(R.string.msg_github_notifications_stack), notifications.size()));
        }
        else
            builder.setContentTitle(context.getString(R.string.msg_github_notifications));

        builder.setContentText(String.format(context.getString(R.string.msg_github_notifications_from), notifications.get(notifications.size() - 1).getRepository().getFullName()));

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
