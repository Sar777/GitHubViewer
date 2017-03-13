package instinctools.android.models.github.commits;

import instinctools.android.models.github.user.UserShort;

public class Commit {
    private String mUrl;
    private String mSha;
    private CommitInfo mCommitInfo;
    private UserShort mAuthor;
    private UserShort mCommitter;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getSha() {
        return mSha;
    }

    public void setSha(String sha) {
        this.mSha = sha;
    }

    public CommitInfo getCommitInfo() {
        return mCommitInfo;
    }

    public void setCommitInfo(CommitInfo commitInfo) {
        this.mCommitInfo = commitInfo;
    }

    public UserShort getAuthor() {
        return mAuthor;
    }

    public void setAuthor(UserShort author) {
        this.mAuthor = author;
    }

    public UserShort getCommitter() {
        return mCommitter;
    }

    public void setCommitter(UserShort committer) {
        this.mCommitter = committer;
    }
}
