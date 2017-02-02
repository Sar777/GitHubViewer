package instinctools.android.models.github.search.enums;

import android.os.Parcel;
import android.os.Parcelable;

public enum RepositorySearchSortType implements Parcelable {
    STARS("stars"),
    FORKS("forks"),
    UPDATED("updated");

    private String mType;

    RepositorySearchSortType(String type) {
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

    public static final Creator<RepositorySearchSortType> CREATOR = new Creator<RepositorySearchSortType>() {
        @Override
        public RepositorySearchSortType createFromParcel(Parcel in) {
            RepositorySearchSortType type = RepositorySearchSortType.values()[in.readInt()];
            type.setType(in.readString());
            return type;
        }

        @Override
        public RepositorySearchSortType[] newArray(int size) {
            return new RepositorySearchSortType[size];
        }
    };

    @Override
    public String toString() {
        return mType;
    }
}
