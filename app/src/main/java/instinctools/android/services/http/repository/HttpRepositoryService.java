package instinctools.android.services.http.repository;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.models.github.repositories.Repository;

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

        operationsRepositories.add(ContentProviderOperation.newDelete(RepositoriesProvider.REPOSITORY_CONTENT_URI)
                .withSelection(DBConstants.REPOSITORY_TYPE + " = ?", new String[]{String.valueOf(mTypeInfo)}).build());

        for (Repository repository : repositories) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RepositoriesProvider.REPOSITORY_CONTENT_URI)
                    .withValue(DBConstants.REPOSITORY_TYPE, mTypeInfo)
                    .withValues(repository.build())
                    .withValues(repository.getRepositoryOwner().build());

            operationsRepositories.add(builder.build());
        }

        try {
            getContentResolver().applyBatch(RepositoriesProvider.AUTHORITY, operationsRepositories);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Content provider exception in onHandleIntent: Type: " + mTypeInfo, e);
        }
    }
}
