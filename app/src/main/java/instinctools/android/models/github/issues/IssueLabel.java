package instinctools.android.models.github.issues;

public class IssueLabel {
    private Integer mId;
    private String mUrl;
    private String mName;
    private String mColor;
    private Boolean mDefault;

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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        this.mColor = color;
    }

    public Boolean getDefault() {
        return mDefault;
    }

    public void setDefault(Boolean default_) {
        this.mDefault = default_;
    }
}