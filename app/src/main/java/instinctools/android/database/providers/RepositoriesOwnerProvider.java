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

public class RepositoriesOwnerProvider extends ContentProvider {

    static final String REPOSITORY_OWNER_PATH = "owners";
    public static final String AUTHORITY = "instinctools.android.providers.RepositoriesOwner";
    public static final Uri REPOSITORY_OWNER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + REPOSITORY_OWNER_PATH);

    static final String REPOSITORY_OWNER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + REPOSITORY_OWNER_PATH;
    static final String REPOSITORY_OWNER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + REPOSITORY_OWNER_PATH;

    static final int URI_REPOSITORIES_OWNER = 1;
    static final int URI_REPOSITORIES_OWNER_ID = 2;

    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, REPOSITORY_OWNER_PATH, URI_REPOSITORIES_OWNER);
        mUriMatcher.addURI(AUTHORITY, REPOSITORY_OWNER_PATH + "/#", URI_REPOSITORIES_OWNER_ID);
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
        builder.setTables(DBConstants.TABLE_REPOSITORY_OWNER);
        switch (mUriMatcher.match(uri)) {
            case URI_REPOSITORIES_OWNER:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = DBConstants.REPOSITORY_OWNER_REPO_ID + " ASC";
                break;
            case URI_REPOSITORIES_OWNER_ID:
                builder.appendWhere(DBConstants.REPOSITORY_OWNER_REPO_ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        Cursor cursor = builder.query(mDB, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), REPOSITORY_OWNER_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (mUriMatcher.match(uri) != URI_REPOSITORIES_OWNER)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        mDB = mDBHelper.getWritableDatabase();
        long rowID = mDB.insert(DBConstants.TABLE_REPOSITORY_OWNER, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(REPOSITORY_OWNER_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_REPOSITORIES_OWNER:
                break;
            case URI_REPOSITORIES_OWNER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.REPOSITORY_OWNER_REPO_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.REPOSITORY_OWNER_REPO_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        int cnt = mDB.delete(DBConstants.TABLE_REPOSITORY_OWNER, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_REPOSITORIES_OWNER:
                break;
            case URI_REPOSITORIES_OWNER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.REPOSITORY_OWNER_REPO_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.REPOSITORY_OWNER_REPO_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        int cnt = mDB.update(DBConstants.TABLE_REPOSITORY_OWNER, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_REPOSITORIES_OWNER:
                return REPOSITORY_OWNER_CONTENT_TYPE;
            case URI_REPOSITORIES_OWNER_ID:
                return REPOSITORY_OWNER_CONTENT_ITEM_TYPE;
            default:
                break;
        }

        return null;
    }
}
