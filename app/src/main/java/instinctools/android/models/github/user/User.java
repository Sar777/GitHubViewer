package instinctools.android.models.github.user;

public class User {
    private int mId;
    private String mLogin;
    private String mAvatarUrl;
    private String mGravatarId;
    private String mName;
    private String mCompany;
    private String mLocation;
    private String mEmail;
    private String mType;
    private Integer mPublicRepos;
    private Integer mPublicGists;
    private String mFollowers;
    private String mFollowing;
    private String mBio;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        this.mLogin = login;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public String getGravatarId() {
        return mGravatarId;
    }

    public void setGravatarId(String gravatarId) {
        this.mGravatarId = gravatarId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        this.mCompany = company;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public Integer getPublicRepos() {
        return mPublicRepos;
    }

    public void setPublicRepos(Integer publicRepos) {
        this.mPublicRepos = publicRepos;
    }

    public Integer getPublicGists() {
        return mPublicGists;
    }

    public void setPublicGists(Integer publicGists) {
        this.mPublicGists = publicGists;
    }

    public String getBio() {
        return mBio;
    }

    public void setBio(String bio) {
        this.mBio = bio;
    }

    public String getFollowers() {
        return mFollowers;
    }

    public void setFollowers(String followers) {
        this.mFollowers = followers;
    }

    public String getFollowing() {
        return mFollowing;
    }

    public void setFollowing(String following) {
        this.mFollowing = following;
    }
}
