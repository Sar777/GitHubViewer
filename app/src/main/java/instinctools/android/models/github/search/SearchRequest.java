package instinctools.android.models.github.search;

import android.os.Parcel;
import android.os.Parcelable;

import instinctools.android.models.github.search.enums.SearchOrderType;
import instinctools.android.models.github.search.enums.SearchType;

public abstract class SearchRequest implements Parcelable {
    protected static final String FIELD_SORT = "sort";
    protected static final String FIELD_ORDER = "order";

    protected final SearchType mType;
    protected final String mText;
    protected SearchOrderType mOrder;

    protected SearchRequest(SearchType type, String text) {
        this.mType = type;
        this.mText = text;
        this.mOrder = SearchOrderType.DESC;
    }

    protected SearchRequest(Parcel in) {
        this.mType = in.readParcelable(SearchType.class.getClassLoader());
        this.mText = in.readString();
        this.mOrder = in.readParcelable(SearchOrderType.class.getClassLoader());
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mType, flags);
        dest.writeString(mText);
        dest.writeParcelable(mOrder, flags);
    }

    public abstract String build();
}
