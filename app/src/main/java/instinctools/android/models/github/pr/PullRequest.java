package instinctools.android.models.github.pr;

import java.util.Date;

public class PullRequest {
    private Integer mId;
    private String mUrl;
    private String mHtmlUrl;
    private Integer mNumber;
    private String mTitle;
    private String mState;
    private Boolean mLocked;
    private Date mCreatedAt;
    private Date mClosedAt;
    private Date mMergedAt;

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

    public Integer getNumber() {
        return mNumber;
    }

    public void setNumber(Integer number) {
        this.mNumber = number;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public Boolean getLocked() {
        return mLocked;
    }

    public void setLocked(Boolean mlocked) {
        this.mLocked = mlocked;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.mCreatedAt = createdAt;
    }

    public Date getClosedAt() {
        return mClosedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.mClosedAt = closedAt;
    }

    public Date getMergedAt() {
        return mMergedAt;
    }

    public void setMergedAt(Date mergedAt) {
        this.mMergedAt = mergedAt;
    }
}
