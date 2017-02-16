package instinctools.android.adapters.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

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
    }
}
