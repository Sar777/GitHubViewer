package instinctools.android.models.github.notification;

import android.content.ContentValues;

import instinctools.android.database.DBConstants;
import instinctools.android.models.github.repositories.Repository;

public class Notification {
    private Integer mId;
    private NotificationRepository mRepository;
    private NotificationSubject mSubject;
    private String mReason;
    private Boolean mUnread;
    private String mUpdateAt;
    private String mLastReadAt;
    private String mUrl;

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

    public ContentValues build() {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NOTIFICATION_ID, mId);
        values.putAll(mRepository.build());
        values.putAll(mSubject.build());
        return values;
    }
}
