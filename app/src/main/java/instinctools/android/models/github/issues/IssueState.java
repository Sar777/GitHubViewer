package instinctools.android.models.github.issues;

import java.util.HashMap;
import java.util.Map;

public enum IssueState {
    OPENED("open"),
    CLOSED("closed");

    private String mState;

    IssueState(final String state) {
        this.mState = state;
    }

    @Override
    public String toString() {
        return mState;
    }

    private static final Map<String, IssueState> mLookup = new HashMap<>();

    static {
        for (IssueState d : IssueState.values()) {
            mLookup.put(d.toString(), d);
        }
    }

    public static IssueState get(String state) {
        IssueState issueState = mLookup.get(state);
        if (issueState == null)
            throw new IllegalArgumentException("Unknown issue state: " + state);

        return issueState;
    }
}
