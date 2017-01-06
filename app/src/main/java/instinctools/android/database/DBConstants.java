package instinctools.android.database;

/**
 * Created by orion on 30.12.16.
 */

public class DBConstants {
    public static final String DB_NAME = "booksDB";
    public static final int DB_VERSION = 1;

    public static final String TABLE_BOOKS = "books";
    public static final String BOOK_ID = "_id";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_DESCRIPTION = "description";
    public static final String BOOK_IMAGE_URL = "image_url";

    public static final String TABLE_BOOKS_CREATE = "CREATE TABLE " + TABLE_BOOKS + "("
            + BOOK_ID + " INTEGER PRIMARY KEY, "
            + BOOK_TITLE + " VARCHAR(100), "
            + BOOK_DESCRIPTION + " TEXT, "
            + BOOK_IMAGE_URL + " VARCHAR(255)" + " );";
}
