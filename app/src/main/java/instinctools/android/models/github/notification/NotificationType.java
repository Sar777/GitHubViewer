package instinctools.android.models.github.notification;

import java.util.HashMap;
import java.util.Map;

public enum NotificationType {
    PULLREQUEST("PullRequest"),
    ISSUE("Issue"),
    RELEASE("Release"),
    COMMIT("Commit");

    private final String mType;

    private static final Map<String, NotificationType> mLookup = new HashMap<>();

    static {
        for (NotificationType d : NotificationType.values()) {
            mLookup.put(d.toString(), d);
        }
    }

    NotificationType(String type) {
        this.mType = type;
    }

    @Override
    public String toString() {
        return mType;
    }

    public static NotificationType get(String type) {
        NotificationType notifyType = mLookup.get(type);
        if (notifyType == null)
            throw new IllegalArgumentException("Unknown notification type: " + type);

        return notifyType;
    }
}
