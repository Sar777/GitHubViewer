package instinctools.android.models.github.issues;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public enum IssueState implements Parcelable {
    OPENED("open"),
    CLOSED("closed");

    private String mState;

    IssueState(final String state) {
        this.mState = state;
    }

    public void setState(String state) {
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
        dest.writeString(mState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<IssueState> CREATOR = new Parcelable.Creator<IssueState>() {
        @Override
        public IssueState createFromParcel(Parcel in) {
            IssueState state = IssueState.values()[in.readInt()];
            state.setState(in.readString());
            return state;
        }

        @Override
        public IssueState[] newArray(int size) {
            return new IssueState[size];
        }
    };

    public static IssueState get(String state) {
        IssueState issueState = mLookup.get(state);
        if (issueState == null)
            throw new IllegalArgumentException("Unknown issue state: " + state);

        return issueState;
    }
}
