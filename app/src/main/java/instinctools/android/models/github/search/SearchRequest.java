package instinctools.android.models.github.search;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchRequest implements Parcelable {
    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DESC = "desc";

    public static final String SORT_STARS = "stars";
    public static final String SORT_FORKS = "forks";
    public static final String SORT_SIZE = "size";

    private String mIn;
    private String mSort;
    private String mOrder;

    public SearchRequest(String in) {
        this.mIn = in;
        this.mOrder = ORDER_DESC;
        this.mSort = SORT_STARS;
    }

    protected SearchRequest(Parcel in) {
        mIn = in.readString();
        mSort = in.readString();
        mOrder = in.readString();
    }

    public String getIn() {
        return mIn;
    }

    public String getSort() {
        return mSort;
    }

    public void setSort(String sort) {
        switch (sort.toLowerCase()) {
            case SORT_STARS:
            case SORT_FORKS:
            case SORT_SIZE:
                break;
            default:
                throw new UnsupportedOperationException("Unsupported sort type: " + sort.toLowerCase());
        }


        this.mSort = sort.toLowerCase();
    }

    public String getOrder() {
        return mOrder;
    }

    public void setOrder(String order) {
        switch (order.toLowerCase()) {
            case ORDER_ASC:
            case ORDER_DESC:
                break;
            default:
                throw new UnsupportedOperationException("Unsupported order type: " + order.toLowerCase());
        }

        this.mOrder = order.toLowerCase();
    }

    @Override
    public String toString() {
        return String.format("?q=%s&sort=%s&order=%s", mIn, mSort, mOrder);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIn);
        dest.writeString(mSort);
        dest.writeString(mOrder);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchRequest> CREATOR = new Creator<SearchRequest>() {
        @Override
        public SearchRequest createFromParcel(Parcel in) {
            return new SearchRequest(in);
        }

        @Override
        public SearchRequest[] newArray(int size) {
            return new SearchRequest[size];
        }
    };
}
