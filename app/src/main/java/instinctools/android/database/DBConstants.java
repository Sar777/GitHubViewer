package instinctools.android.database;

public class DBConstants {
    public static final String DB_NAME = "repositoriesDB";
    static final int DB_VERSION = 1;

    // Repositories
    public static final String TABLE_REPOSITORIES = "repositories";
    public static final String REPOSITORY_ID = "_id";
    public static final String REPOSITORY_TYPE = "type";
    public static final String REPOSITORY_NAME = "name";
    public static final String REPOSITORY_FULLNAME = "full_name";
    public static final String REPOSITORY_HTML_URL = "html_url";
    public static final String REPOSITORY_DESCRIPTION = "description";
    public static final String REPOSITORY_DEFAULT_BRANCH = "default_branch";
    public static final String REPOSITORY_LANGUAGE = "language";
    public static final String REPOSITORY_FORK = "fork";
    public static final String REPOSITORY_PRIVATE = "private";

    // Repositories owner
    public static final String TABLE_REPOSITORY_OWNER = "repository_owner";
    public static final String REPOSITORY_OWNER_REPO_ID = "_id";
    public static final String REPOSITORY_OWNER_TYPE = "type";
    public static final String REPOSITORY_OWNER_ID = "user_id";
    public static final String REPOSITORY_OWNER_LOGIN = "login";
    public static final String REPOSITORY_OWNER_AVATAR_URL_ID = "avatar_url";

    // Watch repositories
    public static final String TABLE_REPOSITORY_WATCH = "watch_repositories";
    // Star repositories
    public static final String TABLE_REPOSITORY_STAR = "star_repositories";

    static final String TABLE_REPOSITORIES_CREATE = "CREATE TABLE " + TABLE_REPOSITORIES + "("
            + REPOSITORY_ID + " INTEGER, "
            + REPOSITORY_TYPE + " INTEGER, "
            + REPOSITORY_NAME + " VARCHAR(100), "
            + REPOSITORY_FULLNAME + " VARCHAR(255), "
            + REPOSITORY_HTML_URL + " VARCHAR(255), "
            + REPOSITORY_DESCRIPTION + " VARCHAR(255), "
            + REPOSITORY_DEFAULT_BRANCH + " VARCHAR(50), "
            + REPOSITORY_LANGUAGE + " VARCHAR(20), "
            + REPOSITORY_FORK + " INTEGER(1), "
            + REPOSITORY_PRIVATE + " INTEGER(1), "
            + " PRIMARY KEY (" + REPOSITORY_ID + ", " + REPOSITORY_TYPE + "))";

    static final String TABLE_REPOSITORY_OWNER_CREATE = "CREATE TABLE " + TABLE_REPOSITORY_OWNER + "("
            + REPOSITORY_OWNER_REPO_ID + " INTEGER, "
            + REPOSITORY_OWNER_TYPE + " INTEGER, "
            + REPOSITORY_OWNER_ID + " INTEGER, "
            + REPOSITORY_OWNER_LOGIN + " VARCHAR(50), "
            + REPOSITORY_OWNER_AVATAR_URL_ID + " VARCHAR(255), "
            + " PRIMARY KEY (" + REPOSITORY_OWNER_REPO_ID + ", " + REPOSITORY_OWNER_TYPE + "))";
}
