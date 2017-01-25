package instinctools.android.models.github.notification;

import android.content.ContentValues;

import instinctools.android.database.DBConstants;

public class NotificationSubject {
    private String mTitle;
    private String mUrl;
    private String mLatestCommentUrl;
    private String mType;

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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public ContentValues build() {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NOTIFICATION_SUBJECT_TITLE, mTitle);
        values.put(DBConstants.NOTIFICATION_SUBJECT_URL, mUrl);
        values.put(DBConstants.NOTIFICATION_SUBJECT_TYPE, mType);
        return values;
    }
}
