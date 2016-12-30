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

public class BooksProvider extends ContentProvider {

    static final String BOOK_PATH = "books";
    public static final String AUTHORITY = "instinctools.android.providers.Books";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BOOK_PATH);

    static final String BOOK_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + BOOK_PATH;
    static final String BOOK_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + BOOK_PATH;

    static final int URI_BOOKS = 1;
    static final int URI_BOOKS_ID = 2;

    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, BOOK_PATH, URI_BOOKS);
        mUriMatcher.addURI(AUTHORITY, BOOK_PATH + "/#", URI_BOOKS_ID);
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
            case URI_BOOKS: {
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = DBConstants.BOOK_TITLE + " ASC";
                break;
            }
            case URI_BOOKS_ID: {
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.BOOK_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.BOOK_ID + " = " + id;

                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        Cursor cursor = mDB.query(DBConstants.TABLE_BOOKS, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), BOOK_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (mUriMatcher.match(uri) != URI_BOOKS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        mDB = mDBHelper.getWritableDatabase();
        long rowID = mDB.insert(DBConstants.TABLE_BOOKS, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(BOOK_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_BOOKS:
                break;
            case URI_BOOKS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.BOOK_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.BOOK_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        int cnt = mDB.delete(DBConstants.TABLE_BOOKS, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_BOOKS:
                break;
            case URI_BOOKS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    selection = DBConstants.BOOK_ID + " = " + id;
                else
                    selection = selection + " AND " + DBConstants.BOOK_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        mDB = mDBHelper.getWritableDatabase();
        int cnt = mDB.update(DBConstants.TABLE_BOOKS, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_BOOKS:
                return BOOK_CONTENT_TYPE;
            case URI_BOOKS_ID:
                return BOOK_CONTENT_ITEM_TYPE;
            default:
                break;
        }

        return null;
    }
}
