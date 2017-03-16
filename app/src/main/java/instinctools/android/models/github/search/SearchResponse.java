package instinctools.android.models.github.search;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.services.github.GitHubResponse;

public class SearchResponse<T> extends GitHubResponse {
    private Integer mTotalCount;
    private Boolean mIncompleteResults;
    private List<T> mResponse;

    public SearchResponse() {
        this.mTotalCount = 0;
        this.mIncompleteResults = true;
        this.mResponse = new ArrayList<>();
    }

    public Integer getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.mTotalCount = totalCount;
    }

    public Boolean getIncompleteResults() {
        return mIncompleteResults;
    }

    public void setIncompleteResults(Boolean incompleteResults) {
        this.mIncompleteResults = incompleteResults;
    }

    public List<T> getResponse() {
        return mResponse;
    }

    public void setResponse(List<T> response) {
        this.mResponse = response;
    }
}