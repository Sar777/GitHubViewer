package instinctools.android.models.github.issues;

import java.util.Date;
import java.util.List;

import instinctools.android.models.github.user.UserShort;

public class Issue {
    private Integer mId;
    private String mUrl;
    private String mHtmlUrl;
    private String mRepositoryUrl;
    private IssueState mState;
    private Integer mNumber;
    private String mTitle;
    private String mBody;
    private UserShort mUser;
    private UserShort mAssignee;
    private Integer mComments;
    private IssuePullRequest mPullRequest;
    private List<IssueLabel> mLabels;
    private Date mCreatedAt;
    private Date mUpdateAt;
    private Date mClosedAt;
    private List<UserShort> mAssignees;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setHtmlUrl(String url) {
        this.mHtmlUrl = url;
    }

    public String getHtmlUrl() {
        return mHtmlUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getRepositoryUrl() {
        return mRepositoryUrl;
    }

    public void setRepositoryUrl(String url) {
        this.mRepositoryUrl = url;
    }

    public IssueState getState() {
        return mState;
    }

    public void setState(IssueState state) {
        this.mState = state;
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

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    public UserShort getUser() {
        return mUser;
    }

    public void setUser(UserShort user) {
        this.mUser = user;
    }

    public UserShort getAssignee() {
        return mAssignee;
    }

    public void setAssignee(UserShort assignee) {
        this.mAssignee = assignee;
    }

    public Integer getComments() {
        return mComments;
    }

    public void setComments(Integer comments) {
        this.mComments = comments;
    }

    public IssuePullRequest getPullRequest() {
        return mPullRequest;
    }

    public void setPullRequest(IssuePullRequest pullRequest) {
        this.mPullRequest = pullRequest;
    }

    public List<IssueLabel> getLabels() {
        return mLabels;
    }

    public void setLabels(List<IssueLabel> labels) {
        this.mLabels = labels;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.mCreatedAt = createdAt;
    }

    public Date getUpdateAt() {
        return mUpdateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.mUpdateAt = updateAt;
    }

    public Date getClosedAt() {
        return mClosedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.mClosedAt = closedAt;
    }

    public List<UserShort> getAssignees() {
        return mAssignees;
    }

    public void setAssignees(List<UserShort> assignees) {
        this.mAssignees = assignees;
    }
}
