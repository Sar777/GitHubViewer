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
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.services.github.user.GithubServiceUser;

public class RepositoriesSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "RepositoriesSyncAdapter";
    ContentResolver mContentResolver;

    public RepositoriesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        this.mContentResolver = context.getContentResolver();
    }

    public RepositoriesSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        this.mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        int syncMask = extras.getInt(Constants.REPOSITORY_SYNC_TYPE);

        if (syncMask == 0 || (syncMask & Constants.REPOSITORY_TYPE_MY) != 0)
            syncMyRepositories();

        if (syncMask == 0 || (syncMask & Constants.REPOSITORY_TYPE_WATCH) != 0)
            syncWatchRepositories();

        if (syncMask == 0 || (syncMask & Constants.REPOSITORY_TYPE_STAR) != 0)
            syncStarsRepositories();

        Log.d(TAG, "Sync repositories success by mask: " + syncMask);
    }

    private void syncMyRepositories() {
        List<Repository> repositories = GithubServiceUser.getMyRepositoryList();
        if (repositories == null)
            return;

        save(repositories, Constants.REPOSITORY_TYPE_MY);
    }

    private void syncWatchRepositories() {
        List<Repository> repositories = GithubServiceUser.getWatchRepositoryList();
        if (repositories == null)
            return;

        save(repositories, Constants.REPOSITORY_TYPE_WATCH);
    }

    private void syncStarsRepositories() {
        List<Repository> repositories = GithubServiceUser.getStarRepositoryList();
        if (repositories == null)
            return;

        save(repositories, Constants.REPOSITORY_TYPE_STAR);
    }

    private void save(List<Repository> repositories, int type) {
        ArrayList<ContentProviderOperation> operationsRepositories = new ArrayList<>(repositories.size());

        operationsRepositories.add(ContentProviderOperation.newDelete(RepositoriesProvider.REPOSITORY_CONTENT_URI)
                .withSelection(DBConstants.REPOSITORY_TYPE + " = ?", new String[]{String.valueOf(type)}).build());

        for (Repository repository : repositories) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RepositoriesProvider.REPOSITORY_CONTENT_URI)
                    .withValue(DBConstants.REPOSITORY_TYPE, type)
                    .withValues(repository.build())
                    .withValues(repository.getRepositoryOwner().build());

            operationsRepositories.add(builder.build());
        }

        try {
            mContentResolver.applyBatch(RepositoriesProvider.AUTHORITY, operationsRepositories);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Content provider exception in onHandleIntent: Type: " + type, e);
        }
    }
}
