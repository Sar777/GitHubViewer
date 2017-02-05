package instinctools.android.models.github.search;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

import instinctools.android.models.github.search.enums.SearchOrderType;
import instinctools.android.models.github.search.enums.SearchType;
import instinctools.android.storages.SettingsStorage;

public class SearchRequest implements Parcelable {
    protected static final String FIELD_SORT = "sort";
    protected static final String FIELD_ORDER = "order";
    protected static final String FIELD_PER_PAGE = "per_page";

    protected final SearchType mType;
    protected final String mText;
    protected SearchOrderType mOrder;
    protected String mSort;
    protected Map<String, String> mFilters;

    protected SearchRequest(SearchType type, String text, SearchOrderType order, String sort, Map<String, String> filters) {
        this.mType = type;
        this.mText = text;
        this.mOrder = order;
        this.mSort = sort;
        this.mFilters = filters;
    }

    protected SearchRequest(Parcel in) {
        this.mType = in.readParcelable(SearchType.class.getClassLoader());
        this.mText = in.readString();
        this.mSort = in.readString();
        this.mOrder = in.readParcelable(SearchOrderType.class.getClassLoader());
        in.readMap(this.mFilters, String.class.getClassLoader());
    }

    public SearchType getType() {
        return mType;
    }

    public String getText() {
        return mText;
    }

    public SearchOrderType getOrder() {
        return mOrder;
    }

    public void setOrder(SearchOrderType order) {
        this.mOrder = order;
    }

    public Map<String, String> getFilters() {
        return mFilters;
    }

    public void setFilters(Map<String, String> filters) {
        this.mFilters = filters;
    }

    public String getSort() {
        return mSort;
    }

    public void setSort(String sort) {
        this.mSort = sort;
    }

    private String filter() {
        String filter = "";
        if (mFilters.isEmpty())
            return filter;

        for (Map.Entry<String, String> fil : mFilters.entrySet()) {
            filter += "+" + fil.getKey() + ":" + fil.getValue();
        }

        return filter;
    }

    public String build() {
        return "/" + mType
                + "?q=" + mText + filter()
                + "&"
                + FIELD_ORDER + "=" + mOrder
                + "&"
                + FIELD_SORT + "=" + mSort
                + "&"
                + FIELD_PER_PAGE + "=" + SettingsStorage.getMaxSearchResult();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mType, flags);
        dest.writeString(mText);
        dest.writeString(mSort);
        dest.writeParcelable(mOrder, flags);
        dest.writeMap(mFilters);
    }
}
