package instinctools.android.models.github.contributors;

import java.util.List;

import instinctools.android.models.github.user.UserContributor;
import instinctools.android.services.github.GitHubResponse;

public class ContributorsListResponse extends GitHubResponse {
    private List<UserContributor> mUsers;

    public ContributorsListResponse(List<UserContributor> users) {
        this.mUsers = users;
    }

    public List<UserContributor> getUsers() {
        return mUsers;
    }
}