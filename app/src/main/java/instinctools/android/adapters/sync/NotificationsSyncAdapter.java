package instinctools.android.adapters.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.constans.Constants;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.NotificationsProvider;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.models.github.notification.NotificationListResponse;
import instinctools.android.services.github.notification.GithubServiceNotifications;

public class NotificationsSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "NotifSyncAdapter";
    ContentResolver mContentResolver;

    public NotificationsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        this.mContentResolver = context.getContentResolver();
    }

    public NotificationsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        this.mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        int syncMask = extras.getInt(Constants.NOTIFICATION_SYNC_TYPE);

        if (syncMask == 0 || (syncMask & Constants.NOTIFICATION_TYPE_ALL) != 0)
            syncAllNotifications();

        if (syncMask == 0 || (syncMask & Constants.NOTIFICATION_TYPE_UNREAD) != 0)
            syncUnreadNotifications();

        if (syncMask == 0 || (syncMask & Constants.NOTIFICATION_TYPE_PARTICIPATING) != 0)
            syncParticipatingNotifications();

        Log.d(TAG, "Sync notifications success by mask: " + syncMask);
    }

    private void syncAllNotifications() {
        NotificationListResponse response = GithubServiceNotifications.getNotificationsResponse(true, false);
        if (response == null)
            return;

        save(response.getNotifications(), Constants.NOTIFICATION_TYPE_ALL);
    }

    private void syncUnreadNotifications() {
        NotificationListResponse response = GithubServiceNotifications.getNotificationsResponse(false, false);
        if (response == null)
            return;

        save(response.getNotifications(), Constants.NOTIFICATION_TYPE_UNREAD);
    }

    private void syncParticipatingNotifications() {
        NotificationListResponse response = GithubServiceNotifications.getNotificationsResponse(false, true);
        if (response == null)
            return;

        save(response.getNotifications(), Constants.NOTIFICATION_TYPE_PARTICIPATING);
    }

    private void save(List<Notification> notifications, int type) {
        ArrayList<ContentProviderOperation> operationsNotifications = new ArrayList<>(notifications.size());

        operationsNotifications.add(ContentProviderOperation.newDelete(NotificationsProvider.NOTIFICATIONS_CONTENT_URI)
                .withSelection(DBConstants.NOTIFICATION_TYPE + " = ?", new String[]{String.valueOf(type)}).build());
        for (Notification notification : notifications) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(NotificationsProvider.NOTIFICATIONS_CONTENT_URI)
                    .withValue(DBConstants.NOTIFICATION_TYPE, type)
                    .withValues(notification.build());

            operationsNotifications.add(builder.build());
        }

        try {
            mContentResolver.applyBatch(NotificationsProvider.AUTHORITY, operationsNotifications);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Content provider. Type: " + type, e);
        }
    }
}
