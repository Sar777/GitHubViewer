package instinctools.android.models.github.notification;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import instinctools.android.database.DBConstants;

public class Notification implements Parcelable {
    private Integer mId;
    private NotificationRepository mRepository;
    private NotificationSubject mSubject;
    private String mReason;
    private Boolean mUnread;
    private String mUpdateAt;
    private String mLastReadAt;
    private String mUrl;

    public Notification() {
    }

    protected Notification(Parcel in) {
        mId = in.readInt();
        mRepository = in.readParcelable(NotificationRepository.class.getClassLoader());
        mSubject = in.readParcelable(NotificationSubject.class.getClassLoader());
        mReason = in.readString();
        mUnread = in.readByte() != 0;
        mUpdateAt = in.readString();
        mLastReadAt = in.readString();
        mUrl = in.readString();
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public NotificationRepository getRepository() {
        return mRepository;
    }

    public void setRepository(NotificationRepository repository) {
        this.mRepository = repository;
    }

    public NotificationSubject getSubject() {
        return mSubject;
    }

    public void setSubject(NotificationSubject subject) {
        this.mSubject = subject;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String mReason) {
        this.mReason = mReason;
    }

    public Boolean getUnread() {
        return mUnread;
    }

    public void setUnread(Boolean unread) {
        this.mUnread = unread;
    }

    public String getUpdateAt() {
        return mUpdateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.mUpdateAt = updateAt;
    }

    public String getLastReadAt() {
        return mLastReadAt;
    }

    public void setLastReadAt(String lastReadAt) {
        this.mLastReadAt = lastReadAt;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        return mId != null ? mId.equals(that.mId) : that.mId == null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeParcelable(mRepository, flags);
        dest.writeParcelable(mSubject, flags);
        dest.writeString(mReason);
        dest.writeByte((byte) (mUnread ? 1 : 0));
        dest.writeString(mUpdateAt);
        dest.writeString(mLastReadAt);
        dest.writeString(mUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public ContentValues build() {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NOTIFICATION_ID, mId);
        values.put(DBConstants.NOTIFICATION_REASON, mReason);
        values.put(DBConstants.NOTIFICATION_URL, mUrl);
        values.put(DBConstants.NOTIFICATION_UNREAD, mUnread);
        values.put(DBConstants.NOTIFICATION_UPDATE_AT, mUpdateAt);
        values.put(DBConstants.NOTIFICATION_LAST_READ_AT, mLastReadAt);
        values.putAll(mRepository.build());
        values.putAll(mSubject.build());
        return values;
    }

    public static Notification fromCursor(Cursor cursor) {
        Notification notification = new Notification();

        notification.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.NOTIFICATION_ID)));
        notification.setReason(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_REASON)));
        notification.setUnread(cursor.getInt(cursor.getColumnIndex(DBConstants.NOTIFICATION_UNREAD)) != 0);
        notification.setUpdateAt(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_UPDATE_AT)));
        notification.setLastReadAt(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_LAST_READ_AT)));
        notification.setUrl(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_URL)));
        notification.setRepository(NotificationRepository.fromCursor(cursor));
        notification.setSubject(NotificationSubject.fromCursor(cursor));

        return notification;
    }
}
