package instinctools.android.models.github.commits;

import java.util.List;

import instinctools.android.services.github.GitHubResponse;

public class CommitsListResponse extends GitHubResponse {
    private List<Commit> mCommits;

    public CommitsListResponse(List<Commit> commits) {
        this.mCommits = commits;
    }

    public List<Commit> getCommits() {
        return mCommits;
    }
}