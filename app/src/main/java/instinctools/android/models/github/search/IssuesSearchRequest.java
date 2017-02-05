package instinctools.android.models.github.search;

import java.util.Map;

import instinctools.android.models.github.search.enums.SearchOrderType;
import instinctools.android.models.github.search.enums.SearchType;

public class IssuesSearchRequest extends SearchRequest {
    public static final String FILTER_AUTHOR = "author";
    public static final String FILTER_ASSIGNEE = "assignee";
    public static final String FILTER_COMMENTS = "comments";
    public static final String FILTER_TYPE = "type";
    public static final String FILTER_STATE = "state";

    public IssuesSearchRequest(String text, SearchOrderType order, String sort, Map<String, String> filters) {
        super(SearchType.ISSUES, text, order, sort, filters);
    }
}
