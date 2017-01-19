package instinctools.android.services;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.RepositoriesOwnerProvider;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.repositories.RepositoryOwner;

public class HttpRepositoryService extends IntentService {
    private static final String TAG = "HttpRepositoryService";

    protected int mTypeInfo;

    HttpRepositoryService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    protected void onHandleIntent(List<Repository> repositories) {
        ArrayList<ContentProviderOperation> operationsRepositories = new ArrayList<>(repositories.size());
        ArrayList<ContentProviderOperation> operationsRepositoriesOwner = new ArrayList<>(repositories.size());

        operationsRepositories.add(ContentProviderOperation.newDelete(RepositoriesProvider.REPOSITORY_CONTENT_URI)
                .withSelection(DBConstants.REPOSITORY_TYPE + "= ?", new String[]{String.valueOf(mTypeInfo)}).build());

        operationsRepositoriesOwner.add(ContentProviderOperation.newDelete(RepositoriesOwnerProvider.REPOSITORY_OWNER_CONTENT_URI)
                .withSelection(DBConstants.REPOSITORY_OWNER_TYPE + "= ?", new String[]{String.valueOf(mTypeInfo)}).build());

        for (Repository repository : repositories) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RepositoriesProvider.REPOSITORY_CONTENT_URI)
                    .withValue(DBConstants.REPOSITORY_TYPE, mTypeInfo)
                    .withValues(repository.build());

            RepositoryOwner repositoryOwner = repository.getRepositoryOwner();
            ContentProviderOperation.Builder builder2 = ContentProviderOperation.newInsert(RepositoriesOwnerProvider.REPOSITORY_OWNER_CONTENT_URI)
                    .withValue(DBConstants.REPOSITORY_OWNER_REPO_ID, repository.getId())
                    .withValue(DBConstants.REPOSITORY_OWNER_TYPE, mTypeInfo)
                    .withValues(repositoryOwner.build());

            operationsRepositories.add(builder.build());
            operationsRepositoriesOwner.add(builder2.build());
        }

        try {
            getContentResolver().applyBatch(RepositoriesProvider.AUTHORITY, operationsRepositories);
            getContentResolver().applyBatch(RepositoriesOwnerProvider.AUTHORITY, operationsRepositoriesOwner);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Content provider exception in onHandleIntent: Type: " + mTypeInfo, e);
        }
    }
}
