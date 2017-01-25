package instinctools.android.services;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.NotificationsProvider;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.services.github.notification.GithubNotifications;
import instinctools.android.services.github.user.GithubServiceUser;
import instinctools.android.storages.SettingsStorage;
import instinctools.android.utility.Services;

public class HttpGithubNotificationsService extends IntentService {
    private static final String TAG = "HttpGithubNotifService";

    public HttpGithubNotificationsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Services.rescheduleAlarmBroadcast(this,
                OnAlarmReceiver.class,
                OnAlarmReceiver.REQUEST_GITHUB_NOTIFICATIONS,
                SettingsStorage.getIntervalUpdateStarsRepo() * 60 * 1000);

        List<Notification> notifications = GithubNotifications.getNotifications(false, false);
        if (notifications == null)
            return;

        ArrayList<ContentProviderOperation> operationsNotifications = new ArrayList<>(notifications.size());

        operationsNotifications.add(ContentProviderOperation.newDelete(NotificationsProvider.NOTIFICATIONS_CONTENT_URI).build());

        for (Notification notification : notifications) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(NotificationsProvider.NOTIFICATIONS_CONTENT_URI)
                    .withValues(notification.build());

            operationsNotifications.add(builder.build());
        }

        try {
            getContentResolver().applyBatch(NotificationsProvider.AUTHORITY, operationsNotifications);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Content provider exception in onHandleIntent", e);
        }
    }
}
