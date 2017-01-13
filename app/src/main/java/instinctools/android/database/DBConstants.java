package instinctools.android.database;

/**
 * Created by orion on 30.12.16.
 */

public class DBConstants {
    public static final String DB_NAME = "repositoriesDB";
    public static final int DB_VERSION = 1;

    public static final String TABLE_REPOSITORIES = "repositories";
    public static final String REPOSITORY_ID = "_id";
    public static final String REPOSITORY_NAME = "name";
    public static final String REPOSITORY_FULLNAME = "full_name";
    public static final String REPOSITORY_DESCRIPTION = "description";
    public static final String REPOSITORY_PRIVATE = "private";

    public static final String TABLE_REPOSITORIES_CREATE = "CREATE TABLE " + TABLE_REPOSITORIES + "("
            + REPOSITORY_ID + " INTEGER PRIMARY KEY, "
            + REPOSITORY_NAME + " VARCHAR(100), "
            + REPOSITORY_FULLNAME + " VARCHAR(255), "
            + REPOSITORY_DESCRIPTION + " VARCHAR(255), "
            + REPOSITORY_PRIVATE + " INTEGER(1)" + " );";
}
