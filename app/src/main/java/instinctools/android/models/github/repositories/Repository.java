package instinctools.android.models.github.repositories;

import android.database.Cursor;

import instinctools.android.database.DBConstants;

public class Repository {
    private int mId;
    private String mName;
    private String mFullName;
    private String mHtmlUrl;
    private String mDescription;
    private String mDefaultBranch;
    private RepositoryOwner mRepositoryOwner;
    private boolean mIsPrivate;
    private boolean mIsFork;
    private String mLanguage;

    public Repository() {
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

    public String getHtmlUrl() {
        return mHtmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.mHtmlUrl = htmlUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getDefaultBranch() {
        return mDefaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.mDefaultBranch = defaultBranch;
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

    public boolean isFork() {
        return mIsFork;
    }

    public void setIsFork(boolean fork) {
        this.mIsFork = fork;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        this.mLanguage = language;
    }

    public static Repository fromCursor(Cursor cursor) {
        Repository repository = new Repository();

        repository.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_ID)));
        repository.setName(cursor.getString(cursor.getColumnIndex(DBConstants.REPOSITORY_NAME)));
        repository.setFullName(cursor.getString(cursor.getColumnIndex(DBConstants.REPOSITORY_FULLNAME)));
        repository.setHtmlUrl(cursor.getString(cursor.getColumnIndex(DBConstants.REPOSITORY_HTML_URL)));
        repository.setDescription(cursor.getString(cursor.getColumnIndex(DBConstants.REPOSITORY_DESCRIPTION)));
        repository.setDefaultBranch(cursor.getString(cursor.getColumnIndex(DBConstants.REPOSITORY_DEFAULT_BRANCH)));
        repository.setLanguage(cursor.getString(cursor.getColumnIndex(DBConstants.REPOSITORY_LANGUAGE)));
        repository.setIsPrivate(cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_PRIVATE)) != 0);
        repository.setIsFork(cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_FORK)) != 0);
        return repository;
    }
}
