package instinctools.android.models.github.user;

public class UserContributor extends UserShort {
    private Integer mContributions;

    public UserContributor() {
    }

    public UserContributor(UserShort user) {
        this.mId = user.mId;
        this.mLogin = user.mLogin;
        this.mGravatarId = user.mGravatarId;
        this.mAvatarUrl = user.mAvatarUrl;
        this.mUrl = user.mUrl;
        this.mHtmlUrl = user.mHtmlUrl;
        this.mType = user.mType;
    }

    public Integer getContributions() {
        return mContributions;
    }

    public void setContributions(Integer contributions) {
        this.mContributions = contributions;
    }
}
