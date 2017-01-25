package instinctools.android.models.github.notification;

import android.content.ContentValues;

import instinctools.android.database.DBConstants;

public class NotificationRepositoryOwner {
    private String mLogin;
    private String mUrl;
    private int mId;
    private String mAvatarUrl;

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        this.mLogin = login;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
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

    public ContentValues build() {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NOTIFICATION_REPO_OWNER_ID, mId);
        values.put(DBConstants.NOTIFICATION_REPO_OWNER_LOGIN, mLogin);
        values.put(DBConstants.NOTIFICATION_REPO_OWNER_AVATAR_URL, mAvatarUrl);
        values.put(DBConstants.NOTIFICATION_REPO_OWNER_URL, mUrl);
        return values;
    }
}
