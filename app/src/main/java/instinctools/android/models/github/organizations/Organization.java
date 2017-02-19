package instinctools.android.models.github.organizations;

public class Organization {
    private String mLogin;
    private Integer mId;
    private String mUrl;
    private String mReposUrl;
    private String mEventsUrl;
    private String mHooksUrl;
    private String mIssuesUrl;
    private String mMembersUrl;
    private String mPublicMembersUrl;
    private String mAvatarUrl;
    private String mDescription;

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        this.mLogin = login;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getReposUrl() {
        return mReposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.mReposUrl = reposUrl;
    }

    public String getEventsUrl() {
        return mEventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.mEventsUrl = eventsUrl;
    }

    public String getHooksUrl() {
        return mHooksUrl;
    }

    public void setHooksUrl(String hooksUrl) {
        this.mHooksUrl = hooksUrl;
    }

    public String getIssuesUrl() {
        return mIssuesUrl;
    }

    public void setIssuesUrl(String issuesUrl) {
        this.mIssuesUrl = issuesUrl;
    }

    public String getMembersUrl() {
        return mMembersUrl;
    }

    public void setMembersUrl(String membersUrl) {
        this.mMembersUrl = membersUrl;
    }

    public String getPublicMembersUrl() {
        return mPublicMembersUrl;
    }

    public void setPublicMembersUrl(String publicMembersUrl) {
        this.mPublicMembersUrl = publicMembersUrl;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
