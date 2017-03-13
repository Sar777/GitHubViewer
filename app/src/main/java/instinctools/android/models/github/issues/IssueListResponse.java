package instinctools.android.models.github.issues;

import java.util.List;

import instinctools.android.services.github.GitHubResponse;

public class IssueListResponse extends GitHubResponse {
    private List<Issue> mIssues;

    public IssueListResponse(List<Issue> issues) {
        this.mIssues = issues;
    }

    public List<Issue> getIssues() {
        return mIssues;
    }
}
