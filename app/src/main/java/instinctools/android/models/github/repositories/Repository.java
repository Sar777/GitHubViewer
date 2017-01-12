package instinctools.android.models.github.repositories;

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
}
