package instinctools.android.services.http.notification;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.NotificationsProvider;
import instinctools.android.models.github.notification.Notification;

public class HttpGithubNotificationService extends IntentService {
    private static final String TAG = "HttpGithubUnNotService";

    protected int mTypeInfo;

    HttpGithubNotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    protected void onHandleIntent(List<Notification> notifications) {
        ArrayList<ContentProviderOperation> operationsNotifications = new ArrayList<>(notifications.size());

        operationsNotifications.add(ContentProviderOperation.newDelete(NotificationsProvider.NOTIFICATIONS_CONTENT_URI)
                .withSelection(DBConstants.NOTIFICATION_TYPE + " = ?", new String[]{String.valueOf(mTypeInfo)}).build());
        for (Notification notification : notifications) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(NotificationsProvider.NOTIFICATIONS_CONTENT_URI)
                    .withValue(DBConstants.NOTIFICATION_TYPE, mTypeInfo)
                    .withValues(notification.build());

            operationsNotifications.add(builder.build());
        }

        try {
            getContentResolver().applyBatch(NotificationsProvider.AUTHORITY, operationsNotifications);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Content provider exception in onHandleIntent: Type: " + mTypeInfo, e);
        }
    }
}
