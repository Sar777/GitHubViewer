package instinctools.android.models.github.notification;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import instinctools.android.database.DBConstants;

public class NotificationSubject implements Parcelable {
    private String mTitle;
    private String mUrl;
    private String mLatestCommentUrl;
    private NotificationType mType;

    public NotificationSubject() {
    }

    private NotificationSubject(Parcel in) {
        mTitle = in.readString();
        mUrl = in.readString();
        mLatestCommentUrl = in.readString();
        mType = in.readParcelable(NotificationType.class.getClassLoader());
    }

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mUrl);
        dest.writeString(mLatestCommentUrl);
        dest.writeParcelable(mType, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationSubject> CREATOR = new Creator<NotificationSubject>() {
        @Override
        public NotificationSubject createFromParcel(Parcel in) {
            return new NotificationSubject(in);
        }

        @Override
        public NotificationSubject[] newArray(int size) {
            return new NotificationSubject[size];
        }
    };

    public ContentValues build() {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NOTIFICATION_SUBJECT_TITLE, mTitle);
        values.put(DBConstants.NOTIFICATION_SUBJECT_URL, mUrl);
        values.put(DBConstants.NOTIFICATION_SUBJECT_LATEST_COMMENT_URL, mLatestCommentUrl);
        values.put(DBConstants.NOTIFICATION_SUBJECT_TYPE, mType.toString());
        return values;
    }

    static NotificationSubject fromCursor(Cursor cursor) {
        NotificationSubject subject = new NotificationSubject();
        subject.setTitle(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_SUBJECT_TITLE)));
        subject.setUrl(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_SUBJECT_URL)));
        subject.setType(NotificationType.get(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_SUBJECT_TYPE))));
        subject.setLatestCommentUrl(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_SUBJECT_LATEST_COMMENT_URL)));

        return subject;
    }
}
