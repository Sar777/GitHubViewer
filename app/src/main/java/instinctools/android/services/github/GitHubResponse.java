package instinctools.android.services.github;

import instinctools.android.models.github.PageLinks;

public class GitHubResponse {
    private PageLinks mPageLinks;

    public PageLinks getPageLinks() {
        return mPageLinks;
    }

    public void setPageLinks(PageLinks pageLinks) {
        this.mPageLinks = pageLinks;
    }
}
