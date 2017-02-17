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

import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.EventsProvider;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.models.github.events.Event;
import instinctools.android.models.github.events.EventsListResponse;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.services.github.events.GithubServiceEvents;
import instinctools.android.services.github.user.GithubServiceUser;

public class EventsSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "EventsSyncAdapter";
    ContentResolver mContentResolver;

    public EventsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        this.mContentResolver = context.getContentResolver();
    }

    public EventsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        this.mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        EventsListResponse eventsResponse = GithubServiceEvents.getEventsResponse();
        if (eventsResponse == null)
            return;

        ArrayList<ContentProviderOperation> operationsRepositories = new ArrayList<>(eventsResponse.getEvents().size());

        operationsRepositories.add(ContentProviderOperation.newDelete(EventsProvider.EVENT_CONTENT_URI).build());

        for (Event event : eventsResponse.getEvents()) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(EventsProvider.EVENT_CONTENT_URI)
                    .withValues(event.build())
                    .withValues(event.getActor().build());

            if (event.getOrg() != null)
                builder.withValues(event.getOrg().build());

            if (event.getRepo() != null)
                builder.withValues(event.getRepo().build());

            operationsRepositories.add(builder.build());
        }

        try {
            mContentResolver.applyBatch(EventsProvider.AUTHORITY, operationsRepositories);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Content provider exception in onPerformSync", e);
        }

        Log.d(TAG, "Sync done");
    }
}
