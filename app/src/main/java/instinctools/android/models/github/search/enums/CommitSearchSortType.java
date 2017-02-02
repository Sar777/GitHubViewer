package instinctools.android.models.github.search.enums;

import android.os.Parcel;
import android.os.Parcelable;

public enum CommitSearchSortType implements Parcelable {
    AUTHOR_DATE("author-date"),
    COMMITTER_DATE("committer-date");

    private String mType;

    CommitSearchSortType(String type) {
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

    public static final Creator<CommitSearchSortType> CREATOR = new Creator<CommitSearchSortType>() {
        @Override
        public CommitSearchSortType createFromParcel(Parcel in) {
            CommitSearchSortType type = CommitSearchSortType.values()[in.readInt()];
            type.setType(in.readString());
            return type;
        }

        @Override
        public CommitSearchSortType[] newArray(int size) {
            return new CommitSearchSortType[size];
        }
    };

    @Override
    public String toString() {
        return mType;
    }
}
