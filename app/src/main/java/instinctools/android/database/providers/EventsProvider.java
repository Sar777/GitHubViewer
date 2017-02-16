package instinctools.android.database.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import instinctools.android.database.DBConstants;
import instinctools.android.database.DBHelper;

public class EventsProvider extends ContentProvider {
    static final String EVENT_PATH = "events";
    public static final String AUTHORITY = "instinctools.android.database.providers.Events";
    public static final Uri EVENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + EVENT_PATH);

    static final String EVENT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + EVENT_PATH;
    static final String EVENT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + EVENT_PATH;

    static final int URI_EVENT = 1;
    static final int URI_EVENT_ID = 2;

    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, EVENT_PATH, URI_EVENT);
        mUriMatcher.addURI(AUTHORITY, EVENT_PATH + "/#", URI_EVENT_ID);
    }

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DBConstants.TABLE_EVENTS);
        switch (mUriMatcher.match(uri)) {
            case URI_EVENT:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = DBConstants.TABLE_EVENTS + "." + DBConstants.EVENT_ID + " ASC";
                break;
            case URI_EVENT_ID:
                builder.appendWhere(DBConstants.TABLE_EVENTS + "." + DBConstants.EVENT_ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        Cursor cursor = builder.query(mDB, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), EVENT_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (mUriMatcher.match(uri) != URI_EVENT)
            throw new IllegalArgumentException("Unsupported uri: " + uri);

        mDB = mDBHelper.getWritableDatabase();
        long rowID = mDB.insert(DBConstants.TABLE_EVENTS, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(EVENT_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_EVENT:
                break;
            case URI_EVENT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.EVENT_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.EVENT_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        int cnt = mDB.delete(DBConstants.TABLE_EVENTS, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_EVENT:
                break;
            case URI_EVENT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.EVENT_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.EVENT_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        int cnt = mDB.update(DBConstants.TABLE_EVENTS, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_EVENT:
                return EVENT_CONTENT_TYPE;
            case URI_EVENT_ID:
                return EVENT_CONTENT_ITEM_TYPE;
            default:
                break;
        }

        return null;
    }
}
