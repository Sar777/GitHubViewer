package instinctools.android.models.github.notification;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import instinctools.android.database.DBConstants;

public class NotificationRepositoryOwner implements Parcelable {
    private String mLogin;
    private String mUrl;
    private int mId;
    private String mAvatarUrl;

    public NotificationRepositoryOwner() {
    }

    private NotificationRepositoryOwner(Parcel in) {
        mLogin = in.readString();
        mUrl = in.readString();
        mId = in.readInt();
        mAvatarUrl = in.readString();
    }

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLogin);
        dest.writeString(mUrl);
        dest.writeInt(mId);
        dest.writeString(mAvatarUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationRepositoryOwner> CREATOR = new Creator<NotificationRepositoryOwner>() {
        @Override
        public NotificationRepositoryOwner createFromParcel(Parcel in) {
            return new NotificationRepositoryOwner(in);
        }

        @Override
        public NotificationRepositoryOwner[] newArray(int size) {
            return new NotificationRepositoryOwner[size];
        }
    };

    public ContentValues build() {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NOTIFICATION_REPO_OWNER_ID, mId);
        values.put(DBConstants.NOTIFICATION_REPO_OWNER_LOGIN, mLogin);
        values.put(DBConstants.NOTIFICATION_REPO_OWNER_AVATAR_URL, mAvatarUrl);
        values.put(DBConstants.NOTIFICATION_REPO_OWNER_URL, mUrl);
        return values;
    }

    static NotificationRepositoryOwner fromCursor(Cursor cursor) {
        NotificationRepositoryOwner owner = new NotificationRepositoryOwner();
        owner.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_OWNER_ID)));
        owner.setUrl(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_OWNER_LOGIN)));
        owner.setUrl(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_OWNER_AVATAR_URL)));
        owner.setUrl(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_OWNER_URL)));
        return owner;
    }
}
