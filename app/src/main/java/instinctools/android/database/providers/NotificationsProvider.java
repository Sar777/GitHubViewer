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

public class NotificationsProvider extends ContentProvider {
    static final String NOTIFICATION_PATH = "notifications";
    public static final String AUTHORITY = "instinctools.android.providers.Notifications";
    public static final Uri NOTIFICATIONS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTIFICATION_PATH);

    static final String NOTIFICATION_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + NOTIFICATION_PATH;
    static final String NOTIFICATION_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + NOTIFICATION_PATH;

    static final int URI_NOTIFICATIONS = 1;
    static final int URI_NOTIFACITONS_ID = 2;

    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, NOTIFICATION_PATH, URI_NOTIFICATIONS);
        mUriMatcher.addURI(AUTHORITY, NOTIFICATION_PATH + "/#", URI_NOTIFACITONS_ID);
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
        builder.setTables(DBConstants.TABLE_NOTIFICATIONS);
        switch (mUriMatcher.match(uri)) {
            case URI_NOTIFICATIONS:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = DBConstants.TABLE_NOTIFICATIONS + "." + DBConstants.NOTIFICATION_ID + " ASC";
                break;
            case URI_NOTIFACITONS_ID:
                builder.appendWhere(DBConstants.TABLE_NOTIFICATIONS + "." + DBConstants.NOTIFICATION_ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        Cursor cursor = builder.query(mDB, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), NOTIFICATIONS_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (mUriMatcher.match(uri) != URI_NOTIFICATIONS)
            throw new IllegalArgumentException("Unsupported uri: " + uri);

        mDB = mDBHelper.getWritableDatabase();
        long rowID = mDB.insert(DBConstants.TABLE_NOTIFICATIONS, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(NOTIFICATIONS_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_NOTIFICATIONS:
                break;
            case URI_NOTIFACITONS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.NOTIFICATION_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.NOTIFICATION_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        int cnt = mDB.delete(DBConstants.TABLE_NOTIFICATIONS, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_NOTIFICATIONS:
                break;
            case URI_NOTIFACITONS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.NOTIFICATION_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.NOTIFICATION_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        int cnt = mDB.update(DBConstants.TABLE_NOTIFICATIONS, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_NOTIFICATIONS:
                return NOTIFICATION_CONTENT_TYPE;
            case URI_NOTIFACITONS_ID:
                return NOTIFICATION_CONTENT_ITEM_TYPE;
            default:
                break;
        }

        return null;
    }
}
