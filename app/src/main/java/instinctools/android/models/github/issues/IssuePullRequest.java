package instinctools.android.models.github.issues;

public class IssuePullRequest {
    private String mUrl;
    private String mHtmlUrl;
    private String mDiffUrl;
    private String mPatchUrl;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getHtmlUrl() {
        return mHtmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.mHtmlUrl = htmlUrl;
    }

    public String getDiffUrl() {
        return mDiffUrl;
    }

    public void setDiffUrl(String diffUrl) {
        this.mDiffUrl = diffUrl;
    }

    public String getPatchUrl() {
        return mPatchUrl;
    }

    public void setPatchUrl(String patchUrl) {
        this.mPatchUrl = patchUrl;
    }
}
