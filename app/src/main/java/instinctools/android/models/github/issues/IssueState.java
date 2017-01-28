package instinctools.android.models.github.issues;

/**
 * Created by orion on 28.1.17.
 */

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
}
