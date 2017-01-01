package instinctools.android.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by orion on 16.12.16.
 */

public class Book implements Parcelable {
    private int mId;
    private String mTitle;
    private String mDescription;
    private String mImage;

    public Book() {
    }

    public Book(int id, String title, String description, String image) {
        this.mId = id;
        this.mTitle = title;
        this.mDescription = description;
        this.mImage = image;
    }

    protected Book(Parcel in) {
        this.mId = in.readInt();
        mTitle = in.readString();
        mDescription = in.readString();
        mImage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mDescription) && !TextUtils.isEmpty(mImage);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public static Book fromCursor(Cursor cursor) {
        return new Book(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("image_url")));
    }
}
