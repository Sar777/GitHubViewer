package instinctools.android.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import instinctools.android.R;
import instinctools.android.adapters.BookAdapter;
import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.database.providers.BooksProvider;
import instinctools.android.decorations.DividerItemDecoration;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivity";

    public static final int PERMISSION_EXTERNAL_STORAGE = 100;

    private static final String BUNDLE_BOOKS = "BOOKS";

    private static final int LOADER_BOOKS_ID = 1;

    public static final String BUNDLE_LOADER_URL = "LOADER_URL";

    private RecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;
    private BooksChangedObserver mBooksChangedObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        requestExternalStoragePermissions();

        getSupportLoaderManager().initLoader(LOADER_BOOKS_ID, null, this);

        // TODO REMOVE ME
        Intent alarmIntent = new Intent(this, OnAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, OnAlarmReceiver.REQUEST_ALARM_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, 10000, pIntent);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_book_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_item_child_layout_margin), ContextCompat.getDrawable(this, R.drawable.line_divider)));

        mBookAdapter = new BookAdapter(this, mRecyclerView, null);
        mRecyclerView.setAdapter(mBookAdapter);

        mBooksChangedObserver = new BooksChangedObserver(new Handler());
        mBooksChangedObserver.observe();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_EXTERNAL_STORAGE && grantResults.length == 2) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(findViewById(R.id.activity_main), R.string.msg_permission_external_storage_granted, Snackbar.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_BOOKS_ID)
            return null;

        return new CursorLoader(this, BooksProvider.BOOK_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mBookAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void requestExternalStoragePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_EXTERNAL_STORAGE);
    }

    @Override
    protected void onDestroy() {
        mBooksChangedObserver.unobserve();
        super.onDestroy();
    }

    private class BooksChangedObserver extends ContentObserver {
        BooksChangedObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            getContentResolver().registerContentObserver(BooksProvider.BOOK_CONTENT_URI, true, this);
        }

        void unobserve() {
            getContentResolver().unregisterContentObserver(this);
        }

        @Override
        public void onChange(boolean selfChange) {
            getSupportLoaderManager().restartLoader(LOADER_BOOKS_ID, null, MainActivity.this);
        }
    }
}
