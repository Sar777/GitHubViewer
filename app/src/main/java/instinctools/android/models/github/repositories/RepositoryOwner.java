package instinctools.android.models.github.repositories;

import android.content.ContentValues;
import android.database.Cursor;

import instinctools.android.database.DBConstants;

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

    public ContentValues build() {
        ContentValues values = new ContentValues();
        values.put(DBConstants.REPOSITORY_OWNER_ID, mId);
        values.put(DBConstants.REPOSITORY_OWNER_LOGIN, mLogin);
        values.put(DBConstants.REPOSITORY_OWNER_AVATAR_URL_ID, mAvatarUrl);
        return values;
    }

    public static RepositoryOwner fromCursor(Cursor cursor) {
        RepositoryOwner repositoryOwner = new RepositoryOwner();

        repositoryOwner.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.TABLE_REPOSITORY_OWNER + "." + DBConstants.REPOSITORY_OWNER_ID)));
        repositoryOwner.setLogin(cursor.getString(cursor.getColumnIndex(DBConstants.TABLE_REPOSITORY_OWNER + "." + DBConstants.REPOSITORY_OWNER_LOGIN)));
        repositoryOwner.setAvatarUrl(cursor.getString(cursor.getColumnIndex(DBConstants.TABLE_REPOSITORY_OWNER + "." + DBConstants.REPOSITORY_OWNER_AVATAR_URL_ID)));
        return repositoryOwner;
    }
}
