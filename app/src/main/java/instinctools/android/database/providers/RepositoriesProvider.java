package instinctools.android.database.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import instinctools.android.database.DBConstants;
import instinctools.android.database.DBHelper;

/**
 * Created by orion on 30.12.16.
 */

public class RepositoriesProvider extends ContentProvider {

    static final String REPOSITORY_PATH = "books";
    public static final String AUTHORITY = "instinctools.android.providers.Repositories";
    public static final Uri REPOSITORY_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + REPOSITORY_PATH);

    static final String REPOSITORY_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + REPOSITORY_PATH;
    static final String REPOSITORY_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + REPOSITORY_PATH;

    static final int URI_REPOSITORIES = 1;
    static final int URI_REPOSITORIES_ID = 2;

    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, REPOSITORY_PATH, URI_REPOSITORIES);
        mUriMatcher.addURI(AUTHORITY, REPOSITORY_PATH + "/#", URI_REPOSITORIES_ID);
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
        switch (mUriMatcher.match(uri)) {
            case URI_REPOSITORIES: {
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = DBConstants.REPOSITORY_ID + " ASC";
                break;
            }
            case URI_REPOSITORIES_ID: {
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.REPOSITORY_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.REPOSITORY_ID + " = " + id;

                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        Cursor cursor = mDB.query(DBConstants.TABLE_REPOSITORIES, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), REPOSITORY_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (mUriMatcher.match(uri) != URI_REPOSITORIES)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        mDB = mDBHelper.getWritableDatabase();
        long rowID = mDB.insert(DBConstants.TABLE_REPOSITORIES, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(REPOSITORY_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_REPOSITORIES:
                break;
            case URI_REPOSITORIES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.REPOSITORY_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.REPOSITORY_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        int cnt = mDB.delete(DBConstants.TABLE_REPOSITORIES, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_REPOSITORIES:
                break;
            case URI_REPOSITORIES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.REPOSITORY_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.REPOSITORY_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        int cnt = mDB.update(DBConstants.TABLE_REPOSITORIES, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_REPOSITORIES:
                return REPOSITORY_CONTENT_TYPE;
            case URI_REPOSITORIES_ID:
                return REPOSITORY_CONTENT_ITEM_TYPE;
            default:
                break;
        }

        return null;
    }
}
