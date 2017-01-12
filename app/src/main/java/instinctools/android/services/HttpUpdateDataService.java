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
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.constans.Constants;
import instinctools.android.models.Book;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.BooksProvider;
import instinctools.android.network.HttpClientFactory;
import instinctools.android.readers.json.JsonTransformer;

/**
 * Created by orion on 30.12.16.
 */

public class HttpUpdateDataService extends IntentService {
    private static final String TAG = "HttpUpdateDataService";

    private static final long INTERVAL_ALARM_PENDING = 10 * 60 * 1000; // 10 min

    public HttpUpdateDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String content = HttpClientFactory.create(Constants.API_URL).addHeader("Accept", "application/json").setMethod("GET").send();
        if (TextUtils.isEmpty(content))
            return;

        List<Book> books = JsonTransformer.transform(content, Book[].class);

        ArrayList<ContentProviderOperation> operations = new ArrayList<>(books.size());
        operations.add(ContentProviderOperation.newDelete(BooksProvider.BOOK_CONTENT_URI).build());

        for (Book book : books) {
            operations.add(ContentProviderOperation.newInsert(BooksProvider.BOOK_CONTENT_URI)
                    .withValue(DBConstants.BOOK_ID, book.getId())
                    .withValue(DBConstants.BOOK_TITLE, book.getTitle())
                    .withValue(DBConstants.BOOK_DESCRIPTION, book.getDescription())
                    .withValue(DBConstants.BOOK_IMAGE_URL, book.getImage())
                    .build());
        }

        try {
            getContentResolver().applyBatch(BooksProvider.AUTHORITY, operations);
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
