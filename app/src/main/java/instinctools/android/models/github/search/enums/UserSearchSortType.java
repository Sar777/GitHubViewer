package instinctools.android.models.github.search.enums;

import android.os.Parcel;
import android.os.Parcelable;

public enum UserSearchSortType implements Parcelable {
    FOLLOWERS("followers"),
    REPOSITORIES("repositories"),
    JOINED("joined");

    private String mType;

    UserSearchSortType(String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
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

    public static final Creator<UserSearchSortType> CREATOR = new Creator<UserSearchSortType>() {
        @Override
        public UserSearchSortType createFromParcel(Parcel in) {
            UserSearchSortType type = UserSearchSortType.values()[in.readInt()];
            type.setType(in.readString());
            return type;
        }

        @Override
        public UserSearchSortType[] newArray(int size) {
            return new UserSearchSortType[size];
        }
    };

    @Override
    public String toString() {
        return mType;
    }
}
