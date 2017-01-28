package instinctools.android.models.github.issues;

import java.util.List;

public class Issue {
    public static final String ISSUE_CLOSE = "closed";
    public static final String ISSUE_OPEN = "open";

    private Integer mId;
    private String mUrl;
    private String mRepositoryUrl;
    private IssueState mState;
    private Integer mNumber;
    private String mTitle;
    private String mBody;
    private List<IssueLabel> mLabels;

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

    public List<IssueLabel> getLabels() {
        return mLabels;
    }

    public void setLabels(List<IssueLabel> labels) {
        this.mLabels = labels;
    }
}
