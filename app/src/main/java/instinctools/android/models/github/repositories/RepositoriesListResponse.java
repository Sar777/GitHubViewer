package instinctools.android.models.github.repositories;

import java.util.List;

import instinctools.android.services.github.GitHubResponse;

public class RepositoriesListResponse extends GitHubResponse {
    private List<Repository> mRepositories;

    public RepositoriesListResponse(List<Repository> repositories) {
        this.mRepositories = repositories;
    }

    public List<Repository> getRepositories() {
        return mRepositories;
    }
}
