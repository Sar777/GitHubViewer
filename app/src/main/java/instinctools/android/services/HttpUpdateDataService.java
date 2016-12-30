package instinctools.android.services;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.constans.Constants;
import instinctools.android.data.Book;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.BooksProvider;
import instinctools.android.network.HttpClientFactory;
import instinctools.android.readers.JsonBookReader;

/**
 * Created by orion on 30.12.16.
 */

public class HttpUpdateDataService extends IntentService {
    private static final String TAG = "HttpUpdateDataService";

    public HttpUpdateDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("ORION", "HANDLE");
        String content = HttpClientFactory.create(Constants.API_URL).addHeader("Accept", "application/json").setMethod("GET").send();
        if (TextUtils.isEmpty(content))
            return;

        List<Book> books = new JsonBookReader().read(content);

        ArrayList<ContentProviderOperation> operations = new ArrayList<>(books.size());
        operations.add(ContentProviderOperation.newDelete(BooksProvider.BOOK_CONTENT_URI).build());

        for (Book book : books) {
            operations.add(ContentProviderOperation.newInsert(BooksProvider.BOOK_CONTENT_URI)
                    .withValue(DBConstants.BOOK_TITLE, book.getTitle())
                    .withValue(DBConstants.BOOK_DESCRIPTION, book.getDescription())
                    .withValue(DBConstants.BOOK_IMAGE_URL, book.getImage())
                    .build());
        }

        try {
            this.getContentResolver().applyBatch(BooksProvider.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Content provider exception in onHandleIntent", e);
        }
    }
}
