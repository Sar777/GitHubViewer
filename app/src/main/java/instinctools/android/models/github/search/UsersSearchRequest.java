package instinctools.android.models.github.search;

import android.os.Parcel;
import android.os.Parcelable;

import instinctools.android.models.github.search.enums.SearchType;
import instinctools.android.models.github.search.enums.UserSearchSortType;

public class UsersSearchRequest extends SearchRequest implements Parcelable {
    private UserSearchSortType mSort;

    public UsersSearchRequest(String text) {
        super(SearchType.USERS, text);
        this.mSort = UserSearchSortType.FOLLOWERS;
    }

    private UsersSearchRequest(Parcel in) {
        super(in);
        this.mSort = in.readParcelable(UserSearchSortType.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(mSort, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UsersSearchRequest> CREATOR = new Creator<UsersSearchRequest>() {
        @Override
        public UsersSearchRequest createFromParcel(Parcel in) {
            return new UsersSearchRequest(in);
        }

        @Override
        public UsersSearchRequest[] newArray(int size) {
            return new UsersSearchRequest[size];
        }
    };

    public UserSearchSortType getSort() {
        return mSort;
    }

    public void setSort(UserSearchSortType sort) {
        this.mSort = sort;
    }

    @Override
    public String build() {
        return "/" + mType + "?q=" + mText + "&" + FIELD_SORT + "=" + mSort + "&" + FIELD_ORDER + "=" + mOrder;
    }
}
