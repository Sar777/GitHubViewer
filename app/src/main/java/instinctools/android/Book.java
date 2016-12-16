package instinctools.android;

import android.text.TextUtils;

/**
 * Created by orion on 16.12.16.
 */

public class Book {
    private String mTitle;
    private String mDescription;
    private String mImage;

    public Book() {
    }

    public Book(String title, String description, String image) {
        this.mTitle = title;
        this.mDescription = description;
        this.mImage = image;
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

    @Override
    public String toString() {
        return "Book{" +
                "mTitle='" + mTitle + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mDescription) && !TextUtils.isEmpty(mImage);
    }
}
