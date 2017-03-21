package instinctools.android.models.github.contents;

public class Content {
    private String mName;
    private String mPath;
    private String mSha;
    private Integer mSize;
    private String mUrl;
    private String mHtmlUrl;
    private ContentType mType;
    private ContentLinks mLinks;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getSha() {
        return mSha;
    }

    public void setSha(String sha) {
        this.mSha = sha;
    }

    public Integer getSize() {
        return mSize;
    }

    public void setSize(Integer size) {
        this.mSize = size;
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

    public ContentType getType() {
        return mType;
    }

    public void setType(ContentType type) {
        this.mType = type;
    }

    public ContentLinks getLinks() {
        return mLinks;
    }

    public void setLinks(ContentLinks links) {
        this.mLinks = links;
    }
}
