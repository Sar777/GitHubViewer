package instinctools.android.models.github.search;

import java.util.Map;

import instinctools.android.models.github.search.enums.SearchOrderType;
import instinctools.android.models.github.search.enums.SearchType;

public class UsersSearchRequest extends SearchRequest {
    public static final String FILTER_REPOS = "repos";
    public static final String FILTER_LOCATION = "location";
    public static final String FILTER_LANGUAGE = "language";
    public static final String FILTER_FOLLOWERS = "followers";
    public static final String FILTER_TYPE = "type";

    public UsersSearchRequest(String text, SearchOrderType order, String sort, Map<String, String> filters) {
        super(SearchType.USERS, text, order, sort, filters);
    }
}
