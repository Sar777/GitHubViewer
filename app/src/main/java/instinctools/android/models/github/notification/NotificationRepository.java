package instinctools.android.models.github.notification;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import instinctools.android.database.DBConstants;

public class NotificationRepository implements Parcelable {
    private Integer mId;
    private String mName;
    private String mHtmlUrl;
    private String mUrl;
    private String mFullName;
    private String mDescription;
    private NotificationRepositoryOwner mOwner;
    private Boolean mFork;
    private Boolean mPrivate;

    public NotificationRepository() {
    }

    private NotificationRepository(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mHtmlUrl = in.readString();
        mUrl = in.readString();
        mFullName = in.readString();
        mDescription = in.readString();
        mOwner = in.readParcelable(NotificationRepositoryOwner.class.getClassLoader());
        mFork = in.readByte() != 0;
        mPrivate = in.readByte() != 0;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getHtmlUrl() {
        return mHtmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.mHtmlUrl = htmlUrl;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setFullName(String fullName) {
        this.mFullName = fullName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public NotificationRepositoryOwner getOwner() {
        return mOwner;
    }

    public void setOwner(NotificationRepositoryOwner owner) {
        this.mOwner = owner;
    }

    public Boolean getFork() {
        return mFork;
    }

    public void setFork(Boolean fork) {
        this.mFork = fork;
    }

    public Boolean getPrivate() {
        return mPrivate;
    }

    public void setPrivate(Boolean private_) {
        this.mPrivate = private_;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mHtmlUrl);
        dest.writeString(mUrl);
        dest.writeString(mFullName);
        dest.writeString(mDescription);
        dest.writeParcelable(mOwner, flags);
        dest.writeByte((byte) (mFork ? 1 : 0));
        dest.writeByte((byte) (mPrivate ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationRepository> CREATOR = new Creator<NotificationRepository>() {
        @Override
        public NotificationRepository createFromParcel(Parcel in) {
            return new NotificationRepository(in);
        }

        @Override
        public NotificationRepository[] newArray(int size) {
            return new NotificationRepository[size];
        }
    };

    public ContentValues build() {
        ContentValues values = new ContentValues();
        values.put(DBConstants.NOTIFICATION_REPO_ID, mId);
        values.put(DBConstants.NOTIFICATION_REPO_NAME, mName);
        values.put(DBConstants.NOTIFICATION_REPO_FULLNAME, mFullName);
        values.put(DBConstants.NOTIFICATION_REPO_DESCRIPION, mDescription);
        values.put(DBConstants.NOTIFICATION_REPO_PRIVATE, mPrivate);
        values.put(DBConstants.NOTIFICATION_REPO_FORK, mFork);
        values.put(DBConstants.NOTIFICATION_REPO_HTML_URL, mHtmlUrl);
        values.put(DBConstants.NOTIFICATION_REPO_URL, mUrl);
        values.putAll(mOwner.build());
        return values;
    }

    static NotificationRepository fromCursor(Cursor cursor) {
        NotificationRepository repository = new NotificationRepository();

        repository.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_ID)));
        repository.setName(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_NAME)));
        repository.setFullName(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_FULLNAME)));
        repository.setDescription(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_DESCRIPION)));
        repository.setPrivate(cursor.getInt(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_PRIVATE)) != 0);
        repository.setFork(cursor.getInt(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_FORK)) != 0);
        repository.setHtmlUrl(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_HTML_URL)));
        repository.setUrl(cursor.getString(cursor.getColumnIndex(DBConstants.NOTIFICATION_REPO_URL)));
        repository.setOwner(NotificationRepositoryOwner.fromCursor(cursor));

        return repository;
    }
}
