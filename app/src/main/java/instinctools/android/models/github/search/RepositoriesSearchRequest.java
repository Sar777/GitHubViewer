package instinctools.android.models.github.search;

import java.util.Map;

import instinctools.android.models.github.search.enums.SearchOrderType;
import instinctools.android.models.github.search.enums.SearchType;

public class RepositoriesSearchRequest extends SearchRequest {
    public static final String FILTER_FORKS = "forks";
    public static final String FILTER_SIZE = "size";
    public static final String FILTER_STARS = "stars";
    public static final String FILTER_FORK = "fork";
    public static final String FILTER_IS = "is";

    public RepositoriesSearchRequest(String text, SearchOrderType order, String sort, Map<String, String> filters) {
        super(SearchType.REPOSITORIES, text, order, sort, filters);
    }
}
