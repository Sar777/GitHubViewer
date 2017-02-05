package instinctools.android.constans;

public class Constants {
    public static final int DISK_MAX_CACHE_SIZE = 1024 * 1024 * 10;
    public static final int MAX_GITHUB_NOTIFICATIONS = 50;

    public static final String CLIENT_ID = "f2b8d4b381f74641a122";
    public static final String CLIENT_SECRET = "74923b8068e754421c46f16ada8d8f0c2d356d7b";
    public static final String CALLBACK_URL = "application://github";
    public static final String SCOPES = "repo,notifications,gist,user";

    // Uri
    public static final String AUTH_CALLBACK_INITIAL = "oauth";

    // Property persistence storage
    public static final String PROPERTY_FIRST_RUN = "FIRST_RUN";

    // For save in database
    public static final int REPOSITORY_TYPE_MY = 0;
    public static final int REPOSITORY_TYPE_WATCH = 1;
    public static final int REPOSITORY_TYPE_STAR = 2;

    public static final int NOTIFICATION_TYPE_UNREAD = 0;
    public static final int NOTIFICATION_TYPE_PARTICIPATING = 1;
    public static final int NOTIFICATION_TYPE_ALL = 2;

    // Interval update repositories. Default
    public static final int INTERVAL_UPDATE_REPO_SERVICES = 10;
    // Interval update notifications. Default
    public static final int INTERVAL_UPDATE_NOTIFICATIONS = 1;
    //
    public static final int MAX_SEARCH_RESULT_BY_PAGE = 30;
}
