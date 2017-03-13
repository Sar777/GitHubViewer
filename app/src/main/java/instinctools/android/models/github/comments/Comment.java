package instinctools.android.models.github.comments;

import java.util.Date;

public class Comment {
    private Integer mId;
    private String mUrl;
    private String mHtmlUrl;
    private String mIssueUrl;
    private Date mCreatedAt;
    private Date mUpdatedAt;
    private String mBody;
    private String mCommentId;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
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

    public String getIssueUrl() {
        return mIssueUrl;
    }

    public void setIssueUrl(String issueUrl) {
        this.mIssueUrl = issueUrl;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.mCreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.mUpdatedAt = updatedAt;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    public String getCommentId() {
        return mCommentId;
    }

    public void setCommentId(String commentId) {
        this.mCommentId = commentId;
    }
}
