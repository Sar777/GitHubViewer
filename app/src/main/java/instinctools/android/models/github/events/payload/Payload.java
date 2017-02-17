package instinctools.android.models.github.events.payload;

import java.util.List;

import instinctools.android.models.github.comments.Comment;
import instinctools.android.models.github.events.enums.PayloadActions;
import instinctools.android.models.github.issues.Issue;
import instinctools.android.models.github.pr.PullRequest;
import instinctools.android.models.github.repositories.Repository;

public class Payload {
    private PayloadActions mAction;
    private Integer mPushId;
    private Integer mSize;
    private String mRef;
    private Issue mIssue;
    private PullRequest mPullRequest;
    private Comment mComment;
    private Repository mForkee;
    private List<PayloadCommit> mCommits;

    public PayloadActions getAction() {
        return mAction;
    }

    public void setAction(PayloadActions action) {
        this.mAction = action;
    }

    public Integer getPushId() {
        return mPushId;
    }

    public void setPushId(Integer pushId) {
        this.mPushId = pushId;
    }

    public Integer getSize() {
        return mSize;
    }

    public void setSize(Integer size) {
        this.mSize = size;
    }

    public String getRef() {
        return mRef;
    }

    public void setRef(String ref) {
        this.mRef = ref;
    }

    public Issue getIssue() {
        return mIssue;
    }

    public void setIssue(Issue issue) {
        this.mIssue = issue;
    }

    public PullRequest getPullRequest() {
        return mPullRequest;
    }

    public void setPullRequest(PullRequest pullRequest) {
        this.mPullRequest = pullRequest;
    }

    public Comment getComment() {
        return mComment;
    }

    public void setComment(Comment comment) {
        this.mComment = comment;
    }

    public Repository getForkee() {
        return mForkee;
    }

    public void setForkee(Repository forkee) {
        this.mForkee = forkee;
    }

    public List<PayloadCommit> getCommits() {
        return mCommits;
    }

    public void setCommits(List<PayloadCommit> commits) {
        this.mCommits = commits;
    }
}
