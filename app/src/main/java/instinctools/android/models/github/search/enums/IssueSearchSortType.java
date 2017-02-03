package instinctools.android.models.github.search.enums;

import android.os.Parcel;
import android.os.Parcelable;

public enum IssueSearchSortType implements Parcelable {
    COMMENTS("comments"),
    CREATED("created"),
    UPDATED("updated");

    private String mType;

    IssueSearchSortType(String type) {
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

    public static final Creator<IssueSearchSortType> CREATOR = new Creator<IssueSearchSortType>() {
        @Override
        public IssueSearchSortType createFromParcel(Parcel in) {
            IssueSearchSortType type = IssueSearchSortType.values()[in.readInt()];
            type.setType(in.readString());
            return type;
        }

        @Override
        public IssueSearchSortType[] newArray(int size) {
            return new IssueSearchSortType[size];
        }
    };

    @Override
    public String toString() {
        return mType;
    }
}
