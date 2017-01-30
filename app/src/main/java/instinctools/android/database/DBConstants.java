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
    public static final String REPOSITORY_FORKS = "forks_count";
    public static final String REPOSITORY_STARGAZERS = "stargazers_count";
    public static final String REPOSITORY_WATCHERS = "watchers_count";
    public static final String REPOSITORY_OPEN_ISSUES = "open_issues_count";
    public static final String REPOSITORY_FORK = "fork";
    public static final String REPOSITORY_PRIVATE = "private";
    public static final String REPOSITORY_OWNER_ID = "user_id";
    public static final String REPOSITORY_OWNER_LOGIN = "login";
    public static final String REPOSITORY_OWNER_AVATAR_URL_ID = "avatar_url";

    // Notifications
    public static final String TABLE_NOTIFICATIONS = "notifcations";
    public static final String NOTIFICATION_ID = "_id";
    public static final String NOTIFICATION_TYPE = "type";
    public static final String NOTIFICATION_REPO_ID = "repo_id";
    public static final String NOTIFICATION_REPO_NAME = "repo_name";
    public static final String NOTIFICATION_REPO_FULLNAME = "repo_fullname";
    public static final String NOTIFICATION_REPO_DESCRIPION = "repo_description";
    public static final String NOTIFICATION_REPO_PRIVATE = "repo_private";
    public static final String NOTIFICATION_REPO_FORK = "repo_fork";
    public static final String NOTIFICATION_REPO_HTML_URL = "repo_html_url";
    public static final String NOTIFICATION_REPO_URL = "repo_url";
    public static final String NOTIFICATION_REPO_OWNER_ID = "repo_owner_id";
    public static final String NOTIFICATION_REPO_OWNER_LOGIN = "repo_owner_login";
    public static final String NOTIFICATION_REPO_OWNER_AVATAR_URL = "repo_owner_avatar_url";
    public static final String NOTIFICATION_REPO_OWNER_URL = "repo_owner_url";
    public static final String NOTIFICATION_SUBJECT_TITLE = "subject_title";
    public static final String NOTIFICATION_SUBJECT_URL = "subject_url";
    public static final String NOTIFICATION_SUBJECT_TYPE = "subject_type";
    public static final String NOTIFICATION_SUBJECT_LATEST_COMMENT_URL = "latest_comment_url";
    public static final String NOTIFICATION_REASON = "reason";
    public static final String NOTIFICATION_UNREAD = "unread";
    public static final String NOTIFICATION_UPDATE_AT = "update_at";
    public static final String NOTIFICATION_LAST_READ_AT = "last_read_at";
    public static final String NOTIFICATION_URL = "url";

    static final String TABLE_REPOSITORIES_CREATE = "CREATE TABLE " + TABLE_REPOSITORIES + "("
            + REPOSITORY_ID + " INTEGER, "
            + REPOSITORY_TYPE + " INTEGER, "
            + REPOSITORY_NAME + " VARCHAR(100), "
            + REPOSITORY_FULLNAME + " VARCHAR(255), "
            + REPOSITORY_HTML_URL + " VARCHAR(255), "
            + REPOSITORY_DESCRIPTION + " VARCHAR(255), "
            + REPOSITORY_DEFAULT_BRANCH + " VARCHAR(50), "
            + REPOSITORY_LANGUAGE + " VARCHAR(20), "
            + REPOSITORY_FORKS + " INTEGER, "
            + REPOSITORY_STARGAZERS + " INTEGER, "
            + REPOSITORY_WATCHERS + " INTEGER, "
            + REPOSITORY_OPEN_ISSUES + " INTEGER, "
            + REPOSITORY_FORK + " INTEGER(1), "
            + REPOSITORY_PRIVATE + " INTEGER(1), "
            + REPOSITORY_OWNER_ID + " INTEGER, "
            + REPOSITORY_OWNER_LOGIN + " VARCHAR(50), "
            + REPOSITORY_OWNER_AVATAR_URL_ID + " VARCHAR(255), "
            + " PRIMARY KEY (" + REPOSITORY_ID + ", " + REPOSITORY_TYPE + "))";

    static final String TABLE_NOTIFICATION_CREATE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
            + NOTIFICATION_ID + " INTEGER, "
            + NOTIFICATION_TYPE + " INTEGER(3), "
            + NOTIFICATION_REPO_ID + " INTEGER, "
            + NOTIFICATION_REPO_NAME + " VARCHAR(30), "
            + NOTIFICATION_REPO_FULLNAME + " VARCHAR(60), "
            + NOTIFICATION_REPO_DESCRIPION + " VARCHAR(255), "
            + NOTIFICATION_REPO_PRIVATE + " INTEGER(1), "
            + NOTIFICATION_REPO_FORK + " INTEGER(1), "
            + NOTIFICATION_REPO_HTML_URL + " VARCHAR(100), "
            + NOTIFICATION_REPO_URL + " VARCHAR(100), "
            + NOTIFICATION_REPO_OWNER_ID + " INTEGER, "
            + NOTIFICATION_REPO_OWNER_LOGIN + " VARCHAR(30), "
            + NOTIFICATION_REPO_OWNER_AVATAR_URL + " VARCHAR(100), "
            + NOTIFICATION_REPO_OWNER_URL + " VARCHAR(100), "
            + NOTIFICATION_SUBJECT_TITLE + " VARCHAR(50), "
            + NOTIFICATION_SUBJECT_URL + " VARCHAR(100), "
            + NOTIFICATION_SUBJECT_LATEST_COMMENT_URL + " VARCHAR(100), "
            + NOTIFICATION_SUBJECT_TYPE + " VARCHAR(20), "
            + NOTIFICATION_REASON + " VARCHAR(20), "
            + NOTIFICATION_URL + " VARCHAR(100), "
            + NOTIFICATION_UNREAD + " INTEGER(1), "
            + NOTIFICATION_UPDATE_AT + " VARCHAR(30), "
            + NOTIFICATION_LAST_READ_AT + " VARCHAR(30), "
            + " PRIMARY KEY (" + NOTIFICATION_ID + ", " + NOTIFICATION_TYPE + "))";
}
