package instinctools.android.models.github.events;

public class EventAuthor {
    private Integer mId;
    private String mLogin;
    private String mGravatarId;
    private String mAvatarUrl;
    private String mUrl;
    private String mHtmlUrl;
    private String mType;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer mId) {
        this.mId = mId;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String mLogin) {
        this.mLogin = mLogin;
    }

    public String getGravatarId() {
        return mGravatarId;
    }

    public void setGravatarId(String gravatarId) {
        this.mGravatarId = gravatarId;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }
}
