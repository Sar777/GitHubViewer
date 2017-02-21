package instinctools.android.models.github.commits;

public class CommitInfo {
    private String mUrl;
    private CommitInfoAuthor mAuthor;
    private CommitInfoAuthor mCommitter;
    private String mMessage;
    private Integer mCommentCount;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public CommitInfoAuthor getAuthor() {
        return mAuthor;
    }

    public void setAuthor(CommitInfoAuthor author) {
        this.mAuthor = author;
    }

    public CommitInfoAuthor getCommitter() {
        return mCommitter;
    }

    public void setCommitter(CommitInfoAuthor committer) {
        this.mCommitter = committer;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public Integer getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.mCommentCount = commentCount;
    }
}
