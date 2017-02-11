package instinctools.android.constans;

public class Constants {
    public static final int DISK_MAX_CACHE_SIZE = 1024 * 1024 * 10;
    public static final int MAX_GITHUB_NOTIFICATIONS = 50;

    public static final int NOTIFICATION_ID = 1;

    public static final String CLIENT_ID = "f2b8d4b381f74641a122";
    public static final String CLIENT_SECRET = "74923b8068e754421c46f16ada8d8f0c2d356d7b";
    public static final String CALLBACK_URL = "application://github";
    public static final String SCOPES = "repo,notifications,gist,user";

    // Uri
    public static final String AUTH_CALLBACK_INITIAL = "oauth";

    // Property persistence storage
    public static final String PROPERTY_FIRST_RUN = "FIRST_RUN";

    public static final String REPOSITORY_SYNC_TYPE = "REPO_SYNC_TYPE";

    // For sync data
    public static final int REPOSITORY_TYPE_MY = 1;
    public static final int REPOSITORY_TYPE_WATCH = 2;
    public static final int REPOSITORY_TYPE_STAR = 4;
    public static final int REPOSITORY_TYPE_ALL = REPOSITORY_TYPE_MY | REPOSITORY_TYPE_WATCH | REPOSITORY_TYPE_STAR;

    public static final String NOTIFICATION_SYNC_TYPE = "NOTIFICATION_SYNC_TYPE";

    public static final int NOTIFICATION_TYPE_UNREAD = 1;
    public static final int NOTIFICATION_TYPE_PARTICIPATING = 2;
    public static final int NOTIFICATION_TYPE_ALL = 4;
    public static final int NOTIFICATION_TYPE_ALL_TYPES = NOTIFICATION_TYPE_UNREAD | NOTIFICATION_TYPE_PARTICIPATING | NOTIFICATION_TYPE_ALL;

    // Interval update repositories. Default
    public static final int INTERVAL_UPDATE_REPO_SERVICES = 10;
    // Interval update notifications. Default
    public static final int INTERVAL_UPDATE_NOTIFICATIONS = 1;
    //
    public static final int MAX_SEARCH_RESULT_BY_PAGE = 30;
}
