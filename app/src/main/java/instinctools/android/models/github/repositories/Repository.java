package instinctools.android.models.github.repositories;

import android.database.Cursor;
import android.text.TextUtils;

import instinctools.android.database.DBConstants;

/**
 * Created by orion on 12.1.17.
 */

public class Repository {
    private int mId;
    private String mName;
    private String mFullName;
    private RepositoryOwner mRepositoryOwner;
    private boolean mIsPrivate;
    private String mDescription;

    public Repository() {
    }

    public Repository(int id, String name, String fullName, String description, int isPrivate) {
        this.mId = id;
        this.mName = name;
        this.mFullName = TextUtils.isEmpty(fullName) ? "" : fullName;;
        this.mDescription = TextUtils.isEmpty(description) ? "" : description;
        this.mIsPrivate = isPrivate != 0;

        // Not used
        this.mRepositoryOwner = new RepositoryOwner();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        this.mFullName = fullName;
    }

    public RepositoryOwner getRepositoryOwner() {
        return mRepositoryOwner;
    }

    public void setRepositoryOwner(RepositoryOwner repositoryOwner) {
        this.mRepositoryOwner = repositoryOwner;
    }

    public boolean isPrivate() {
        return mIsPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.mIsPrivate = isPrivate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public static Repository fromCursor(Cursor cursor) {
        return new Repository(cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_ID)),
                cursor.getString(cursor.getColumnIndex(DBConstants.REPOSITORY_NAME)),
                cursor.getString(cursor.getColumnIndex(DBConstants.REPOSITORY_FULLNAME)),
                cursor.getString(cursor.getColumnIndex(DBConstants.REPOSITORY_DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_PRIVATE)));
    }
}
