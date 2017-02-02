package instinctools.android.models.github.commits;

import java.util.Date;

public class Commit {
    private String mHash;
    private String mMessage;
    private Integer mAuthorId;
    private String mAuthorName;
    private String mAuthorEmail;
    private Date mAuthorDate;
    private Integer mCommitterId;
    private String mCommitterName;
    private String mCommitterEmail;
    private Date mCommitterDate;
    private String mUrl;
    private String mHtmlUrl;

    public String getHash() {
        return mHash;
    }

    public void setHash(String hash) {
        this.mHash = hash;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public Integer getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(int authorId) {
        this.mAuthorId = authorId;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        this.mAuthorName = authorName;
    }

    public String getAuthorEmail() {
        return mAuthorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.mAuthorEmail = authorEmail;
    }

    public Date getAuthorDate() {
        return mAuthorDate;
    }

    public void setAuthorDate(Date authorDate) {
        this.mAuthorDate = authorDate;
    }

    public Integer getCommitterId() {
        return mCommitterId;
    }

    public void setCommitterId(Integer committerId) {
        this.mCommitterId = committerId;
    }

    public String getCommitterName() {
        return mCommitterName;
    }

    public void setCommitterName(String committerName) {
        this.mCommitterName = committerName;
    }

    public String getCommitterEmail() {
        return mCommitterEmail;
    }

    public void setCommitterEmail(String commiterEmail) {
        this.mCommitterEmail = commiterEmail;
    }

    public Date getCommitterDate() {
        return mCommitterDate;
    }

    public void setCommitterDate(Date committerDate) {
        this.mCommitterDate = committerDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getHtmlUrl() {
        return mHtmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.mHtmlUrl = htmlUrl;
    }
}
