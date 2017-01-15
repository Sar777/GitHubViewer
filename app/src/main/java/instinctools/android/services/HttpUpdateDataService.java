package instinctools.android.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.services.github.GithubServices;

public class HttpUpdateDataService extends IntentService {
    private static final String TAG = "HttpUpdateDataService";

    private static final long INTERVAL_ALARM_PENDING = 10 * 60 * 1000; // 10 min

    public HttpUpdateDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Repository> repositories = GithubServices.getRepositoryList();
        if (repositories == null)
            return;

        ArrayList<ContentProviderOperation> operations = new ArrayList<>(repositories.size());
        operations.add(ContentProviderOperation.newDelete(RepositoriesProvider.REPOSITORY_CONTENT_URI).build());

        for (Repository repository : repositories) {
            operations.add(ContentProviderOperation.newInsert(RepositoriesProvider.REPOSITORY_CONTENT_URI)
                    .withValue(DBConstants.REPOSITORY_ID, repository.getId())
                    .withValue(DBConstants.REPOSITORY_NAME, repository.getName())
                    .withValue(DBConstants.REPOSITORY_FULLNAME, repository.getFullName())
                    .withValue(DBConstants.REPOSITORY_DESCRIPTION, repository.getDescription())
                    .withValue(DBConstants.REPOSITORY_LANGUAGE, repository.getLanguage())
                    .withValue(DBConstants.REPOSITORY_PRIVATE, repository.isPrivate())
                    .withValue(DBConstants.REPOSITORY_FORK, repository.isFork())
                    .build());
        }

        try {
            getContentResolver().applyBatch(RepositoriesProvider.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Content provider exception in onHandleIntent", e);
        }

        Intent alarmIntent = new Intent(this, OnAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, OnAlarmReceiver.REQUEST_ALARM_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarm.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVAL_ALARM_PENDING, pIntent);
        else
            alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVAL_ALARM_PENDING, pIntent);
    }
}
