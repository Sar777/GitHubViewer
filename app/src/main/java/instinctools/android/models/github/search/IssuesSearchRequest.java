package instinctools.android.models.github.search;

import android.os.Parcel;
import android.os.Parcelable;

import instinctools.android.models.github.search.enums.IssueSearchSortType;
import instinctools.android.models.github.search.enums.SearchType;
import instinctools.android.models.github.search.enums.UserSearchSortType;

public class IssuesSearchRequest extends SearchRequest implements Parcelable {
    private IssueSearchSortType mSort;

    public IssuesSearchRequest(String text) {
        super(SearchType.ISSUES, text);
        this.mSort = IssueSearchSortType.UPDATED;
    }

    private IssuesSearchRequest(Parcel in) {
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

    public static final Creator<IssuesSearchRequest> CREATOR = new Creator<IssuesSearchRequest>() {
        @Override
        public IssuesSearchRequest createFromParcel(Parcel in) {
            return new IssuesSearchRequest(in);
        }

        @Override
        public IssuesSearchRequest[] newArray(int size) {
            return new IssuesSearchRequest[size];
        }
    };

    public IssueSearchSortType getSort() {
        return mSort;
    }

    public void setSort(IssueSearchSortType sort) {
        this.mSort = sort;
    }

    @Override
    public String build() {
        return super.build();
    }
}
