package instinctools.android.models.github.notification;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public enum NotificationType implements Parcelable {
    PULLREQUEST("PullRequest"),
    ISSUE("Issue"),
    RELEASE("Release"),
    COMMIT("Commit");

    private String mType;

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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
        dest.writeString(mType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<NotificationType> CREATOR = new Parcelable.Creator<NotificationType>() {
        @Override
        public NotificationType createFromParcel(Parcel in) {
            NotificationType type = NotificationType.values()[in.readInt()];
            type.setType(in.readString());
            return type;
        }

        @Override
        public NotificationType[] newArray(int size) {
            return new NotificationType[size];
        }
    };

    public void setType(String type) {
        mType = type;
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
