package instinctools.android.models.github.search;

import android.os.Parcel;
import android.os.Parcelable;

import instinctools.android.models.github.search.enums.CommitSearchSortType;
import instinctools.android.models.github.search.enums.RepositorySearchSortType;
import instinctools.android.models.github.search.enums.SearchType;

public class CommitsSearchRequest extends SearchRequest implements Parcelable {
    /// TODO FIX ME. NOT SUPPORTED SORT TYPE
    private CommitSearchSortType mSort;

    public CommitsSearchRequest(String text) {
        super(SearchType.COMMITS, text);
        this.mSort = CommitSearchSortType.COMMITTER_DATE;
    }

    private CommitsSearchRequest(Parcel in) {
        super(in);
        this.mSort = in.readParcelable(RepositorySearchSortType.class.getClassLoader());
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

    public static final Creator<CommitsSearchRequest> CREATOR = new Creator<CommitsSearchRequest>() {
        @Override
        public CommitsSearchRequest createFromParcel(Parcel in) {
            return new CommitsSearchRequest(in);
        }

        @Override
        public CommitsSearchRequest[] newArray(int size) {
            return new CommitsSearchRequest[size];
        }
    };

    public CommitSearchSortType getSort() {
        return mSort;
    }

    public void setSort(CommitSearchSortType sort) {
        this.mSort = sort;
    }

    @Override
    public String build() {
        return "/" + mType + "?q=" + mText + "&" + FIELD_ORDER + "=" + mOrder;
    }
}
