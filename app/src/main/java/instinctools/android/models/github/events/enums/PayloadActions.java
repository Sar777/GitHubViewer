package instinctools.android.models.github.events.enums;

import java.util.HashMap;
import java.util.Map;

public enum PayloadActions {
    STARTED("started"),
    PUBLISHED("published"),
    ADDED("added"),
    OPENED("opened"),
    REOPENED("reopened"),
    CLOSED("closed"),
    CREATED("created");

    private String mAction;

    private static final Map<String, PayloadActions> mActions = new HashMap<>();

    static {
        for (PayloadActions type : PayloadActions.values()) {
            mActions.put(type.toString(), type);
        }
    }

    PayloadActions(String type) {
        this.mAction = type;
    }

    public void setType(String type) {
        this.mAction = type;
    }

    public static PayloadActions get(String type) {
        PayloadActions action = mActions.get(type);
        if (action == null)
            throw new IllegalArgumentException("Unknown action type: " + type);

        return action;
    }

    @Override
    public String toString() {
        return mAction;
    }
}
