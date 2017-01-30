package instinctools.android.models.github.notification;

import android.content.ContentValues;
import android.database.Cursor;

import instinctools.android.database.DBConstants;

public class NotificationSubject {
    private String mTitle;
    private String mUrl;
    private String mLatestCommentUrl;
    private NotificationType mType;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getLatestCommentUrl() {
        return mLatestCommentUrl;
    }

    public void setLatestCommentUrl(String latestCommentUrl) {
        this.mLatestCommentUrl = latestCommentUrl;
    }

    public NotificationType getType() {
        return mType;
    }

    public void setType(NotificationType type) {
        this.mType = type;
    }

    public ContentValues build() {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NOTIFICATION_SUBJECT_TITLE, mTitle);
        values.put(DBConstants.NOTIFICATION_SUBJECT_URL, mUrl);
        values.put(DBConstants.NOTIFICATION_SUBJECT_LATEST_COMMENT_URL, mLatestCommentUrl);
        values.put(DBConstants.NOTIFICATION_SUBJECT_TYPE, mType.toString());
        return values;
    }

    public static NotificationSubject fromCursor(Cursor cursor) {
        NotificationSubject subject = new NotificationSubject();
        subject.setTitle(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_SUBJECT_TITLE)));
        subject.setUrl(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_SUBJECT_URL)));
        subject.setType(NotificationType.get(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_SUBJECT_TYPE))));
        subject.setLatestCommentUrl(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_SUBJECT_LATEST_COMMENT_URL)));

        return subject;
    }
}
