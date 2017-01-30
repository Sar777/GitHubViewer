package instinctools.android.models.github.repositories;

import android.content.ContentValues;
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
    private int mForks;
    private int mStargazers;
    private int mWatchers;
    private int mOpenIssues;

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

    public int getForks() {
        return mForks;
    }

    public void setForks(int forks) {
        this.mForks = forks;
    }

    public int getStargazers() {
        return mStargazers;
    }

    public void setStargazers(int stargazers) {
        this.mStargazers = stargazers;
    }

    public int getWatchers() {
        return mWatchers;
    }

    public void setWatchers(int watchers) {
        this.mWatchers = watchers;
    }

    public int getOpenIssues() {
        return mOpenIssues;
    }

    public void setOpenIssues(int openIssues) {
        this.mOpenIssues = openIssues;
    }

    public ContentValues build() {
        ContentValues values = new ContentValues();

        values.put(DBConstants.REPOSITORY_ID, mId);
        values.put(DBConstants.REPOSITORY_NAME, mName);
        values.put(DBConstants.REPOSITORY_FULLNAME, mFullName);
        values.put(DBConstants.REPOSITORY_HTML_URL, mHtmlUrl);
        values.put(DBConstants.REPOSITORY_DESCRIPTION, mDescription);
        values.put(DBConstants.REPOSITORY_DEFAULT_BRANCH, mDefaultBranch);
        values.put(DBConstants.REPOSITORY_LANGUAGE, mLanguage);
        values.put(DBConstants.REPOSITORY_FORKS, mForks);
        values.put(DBConstants.REPOSITORY_STARGAZERS, mStargazers);
        values.put(DBConstants.REPOSITORY_WATCHERS, mWatchers);
        values.put(DBConstants.REPOSITORY_OPEN_ISSUES, mOpenIssues);
        values.put(DBConstants.REPOSITORY_PRIVATE, mIsPrivate);
        values.put(DBConstants.REPOSITORY_FORK, mIsFork);
        return values;
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
        repository.setForks(cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_FORKS)));
        repository.setStargazers(cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_STARGAZERS)));
        repository.setWatchers(cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_WATCHERS)));
        repository.setOpenIssues(cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_OPEN_ISSUES)));
        repository.setIsPrivate(cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_PRIVATE)) != 0);
        repository.setIsFork(cursor.getInt(cursor.getColumnIndex(DBConstants.REPOSITORY_FORK)) != 0);
        repository.setRepositoryOwner(RepositoryOwner.fromCursor(cursor));
        return repository;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mFullName='" + mFullName + '\'' +
                ", mHtmlUrl='" + mHtmlUrl + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mDefaultBranch='" + mDefaultBranch + '\'' +
                ", mRepositoryOwner=" + mRepositoryOwner +
                ", mIsPrivate=" + mIsPrivate +
                ", mIsFork=" + mIsFork +
                ", mLanguage='" + mLanguage + '\'' +
                ", mForks=" + mForks +
                ", mStargazers=" + mStargazers +
                ", mWatchers=" + mWatchers +
                ", mOpenIssues=" + mOpenIssues +
                '}';
    }
}
