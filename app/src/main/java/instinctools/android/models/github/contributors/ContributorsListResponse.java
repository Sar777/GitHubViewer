package instinctools.android.models.github.contributors;

import java.util.List;

import instinctools.android.models.github.PageLinks;
import instinctools.android.models.github.user.UserContributor;

public class ContributorsListResponse {
    private List<UserContributor> mUsers;
    private PageLinks mPageLinks;

    public ContributorsListResponse(List<UserContributor> users) {
        this.mUsers = users;
    }

    public List<UserContributor> getUsers() {
        return mUsers;
    }

    public PageLinks getPageLinks() {
        return mPageLinks;
    }

    public void setPageLinks(PageLinks pageLinks) {
        this.mPageLinks = pageLinks;
    }
}