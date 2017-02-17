package instinctools.android.models.github.events.payload;

public class PayloadCommit {
    private String mSha;
    private PayloadCommitAuthor mAuthor;
    private String mMessage;
    private String mUrl;

    public String getSha() {
        return mSha;
    }

    public void setSha(String sha) {
        this.mSha = sha;
    }

    public PayloadCommitAuthor getAuthor() {
        return mAuthor;
    }

    public void setAuthor(PayloadCommitAuthor author) {
        this.mAuthor = author;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }
}
