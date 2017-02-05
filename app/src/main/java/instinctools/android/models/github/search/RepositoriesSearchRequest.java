package instinctools.android.models.github.search;

import android.os.Parcel;
import android.os.Parcelable;

import instinctools.android.models.github.search.enums.RepositorySearchSortType;
import instinctools.android.models.github.search.enums.SearchType;

public class RepositoriesSearchRequest extends SearchRequest implements Parcelable {
    private RepositorySearchSortType mSort;

    public RepositoriesSearchRequest(String text) {
        super(SearchType.REPOSITORIES, text);
        this.mSort = RepositorySearchSortType.UPDATED;
    }

    private RepositoriesSearchRequest(Parcel in) {
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

    public static final Creator<RepositoriesSearchRequest> CREATOR = new Creator<RepositoriesSearchRequest>() {
        @Override
        public RepositoriesSearchRequest createFromParcel(Parcel in) {
            return new RepositoriesSearchRequest(in);
        }

        @Override
        public RepositoriesSearchRequest[] newArray(int size) {
            return new RepositoriesSearchRequest[size];
        }
    };

    public RepositorySearchSortType getSort() {
        return mSort;
    }

    public void setSort(RepositorySearchSortType sort) {
        this.mSort = sort;
    }

    @Override
    public String build() {
        return super.build();
    }
}
