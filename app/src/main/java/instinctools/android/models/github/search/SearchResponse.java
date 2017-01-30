package instinctools.android.models.github.search;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.models.github.repositories.Repository;

public class SearchResponse {
    private Integer mTotalCount;
    private Boolean mIncompleteResults;
    private List<Repository> mRepositories;

    public SearchResponse() {
        this.mTotalCount = 0;
        this.mIncompleteResults = true;
        this.mRepositories = new ArrayList<>();
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

    public List<Repository> getRepositories() {
        return mRepositories;
    }

    public void setRepositories(List<Repository> repositories) {
        this.mRepositories = repositories;
    }
}