package instinctools.android.models.github.user;

/**
 * Created by orion on 13.1.17.
 */

public class User {
    private int mId;
    private String mLogin;
    private String mAvatarUrl;
    private String mGravatarId;
    private String mName;
    private String mCompany;
    private String mLocation;
    private String mEmail;
    private int mPublicRepos;
    private int mPublicGists;

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

    public String getmEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public int getPublicRepos() {
        return mPublicRepos;
    }

    public void setPublicRepos(int publicRepos) {
        this.mPublicRepos = publicRepos;
    }

    public int getPublicGists() {
        return mPublicGists;
    }

    public void setPublicGists(int publicGists) {
        this.mPublicGists = publicGists;
    }

    @Override
    public String toString() {
        return "User{" +
                "mId=" + mId +
                ", mLogin='" + mLogin + '\'' +
                ", mAvatarUrl='" + mAvatarUrl + '\'' +
                ", mGravatarId='" + mGravatarId + '\'' +
                ", mName='" + mName + '\'' +
                ", mCompany='" + mCompany + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mPublicRepos='" + mPublicRepos + '\'' +
                ", mPublicGists='" + mPublicGists + '\'' +
                '}';
    }
}
