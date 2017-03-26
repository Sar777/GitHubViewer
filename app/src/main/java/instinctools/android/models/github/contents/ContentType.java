package instinctools.android.models.github.contents;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public enum ContentType implements Parcelable {
    FILE("file"),
    DIR("dir"),
    SYMLINK("symlink");

    private String mType;

    ContentType(final String state) {
        this.mType = state;
    }

    public void setType(String state) {
        this.mType = state;
    }

    @Override
    public String toString() {
        return mType;
    }

    private static final Map<String, ContentType> mLookup = new HashMap<>();

    static {
        for (ContentType d : ContentType.values()) {
            mLookup.put(d.toString(), d);
        }
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

    public static final Parcelable.Creator<ContentType> CREATOR = new Parcelable.Creator<ContentType>() {
        @Override
        public ContentType createFromParcel(Parcel in) {
            ContentType state = ContentType.values()[in.readInt()];
            state.setType(in.readString());
            return state;
        }

        @Override
        public ContentType[] newArray(int size) {
            return new ContentType[size];
        }
    };

    public static ContentType get(String state) {
        ContentType type = mLookup.get(state);
        if (type == null)
            throw new IllegalArgumentException("Unknown file type: " + state);

        return type;
    }
}
