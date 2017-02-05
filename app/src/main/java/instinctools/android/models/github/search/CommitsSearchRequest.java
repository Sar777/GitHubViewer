package instinctools.android.models.github.search;

import java.util.Map;

import instinctools.android.models.github.search.enums.SearchOrderType;
import instinctools.android.models.github.search.enums.SearchType;

public class CommitsSearchRequest extends SearchRequest {
    public static final String FILTER_AUTHOR = "author";
    public static final String FILTER_COMMITTER = "committer";
    public static final String FILTER_MERGE = "merge";

    public CommitsSearchRequest(String text, SearchOrderType order, String sort, Map<String, String> filters) {
        super(SearchType.COMMITS, text, order, sort, filters);
    }
}
