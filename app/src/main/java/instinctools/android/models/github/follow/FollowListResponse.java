package instinctools.android.models.github.follow;

import java.util.List;

import instinctools.android.models.github.user.UserShort;
import instinctools.android.services.github.GitHubResponse;

public class FollowListResponse extends GitHubResponse {
    private List<UserShort> mUsers;

    public FollowListResponse(List<UserShort> users) {
        this.mUsers = users;
    }

    public List<UserShort> getUsers() {
        return mUsers;
    }
}
