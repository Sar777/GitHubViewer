package instinctools.android.models.github.search.enums;

import android.os.Parcel;
import android.os.Parcelable;

public enum SearchOrderType implements Parcelable {
    ASC("asc"),
    DESC("desc");

    private String mType;

    SearchOrderType(String type) {
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

    public static final Creator<SearchOrderType> CREATOR = new Creator<SearchOrderType>() {
        @Override
        public SearchOrderType createFromParcel(Parcel in) {
            SearchOrderType type = SearchOrderType.values()[in.readInt()];
            type.setType(in.readString());
            return type;
        }

        @Override
        public SearchOrderType[] newArray(int size) {
            return new SearchOrderType[size];
        }
    };

    @Override
    public String toString() {
        return mType;
    }
}