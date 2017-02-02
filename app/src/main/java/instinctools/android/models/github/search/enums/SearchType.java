package instinctools.android.models.github.search.enums;

import android.os.Parcel;
import android.os.Parcelable;

public enum SearchType implements Parcelable {
    REPOSITORIES("repositories"),
    COMMITS("commits"),
    ISSUES("issues"),
    USERS("users");

    private String mType;

    SearchType(String type) {
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

    public static final Creator<SearchType> CREATOR = new Creator<SearchType>() {
        @Override
        public SearchType createFromParcel(Parcel in) {
            SearchType type = SearchType.values()[in.readInt()];
            type.setType(in.readString());
            return type;
        }

        @Override
        public SearchType[] newArray(int size) {
            return new SearchType[size];
        }
    };

    @Override
    public String toString() {
        return mType;
    }
}
