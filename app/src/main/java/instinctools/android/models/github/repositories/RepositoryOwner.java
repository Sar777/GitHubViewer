package instinctools.android.models.github.repositories;

/**
 * Created by orion on 12.1.17.
 */
public class RepositoryOwner {

    private String mLogin;
    private int mId;
    private String mAvatarUrl;

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        this.mLogin = login;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }
}
