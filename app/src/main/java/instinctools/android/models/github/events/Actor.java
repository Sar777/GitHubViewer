package instinctools.android.models.github.events;

import android.content.ContentValues;

import instinctools.android.database.DBConstants;

public class Actor {
    private Integer mId;
    private String mLogin;
    private String mGravatarId;
    private String mAvatarUrl;
    private String mUrl;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        this.mLogin = login;
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

    public ContentValues build() {
        ContentValues values = new ContentValues();

        values.put(DBConstants.EVENT_ACTOR_ID, mId);
        values.put(DBConstants.EVENT_ACTOR_LOGIN, mLogin);
        values.put(DBConstants.EVENT_ACTOR_URL, mUrl);
        values.put(DBConstants.EVENT_ACTOR_AVATAR_URL, mAvatarUrl);
        values.put(DBConstants.EVENT_ACTOR_GRAVATAR_ID, mGravatarId);
        return values;
    }
}
