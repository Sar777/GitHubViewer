package instinctools.android.models.github.repositories;

public class RepositoryReadme {
    private String mType;
    private String mEncoding;
    private Integer mSize;
    private String mName;
    private String mPath;
    private String mContent;
    private String mSha;
    private String mUrl;
    private String mGitUrl;
    private String mHtmlUrl;
    private String mDownloadUrl;
    private RepositoryReadmeLinks mLinks;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getEncoding() {
        return mEncoding;
    }

    public void setEncoding(String encoding) {
        this.mEncoding = encoding;
    }

    public Integer getSize() {
        return mSize;
    }

    public void setSize(Integer size) {
        this.mSize = size;
    }

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

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public String getSha() {
        return mSha;
    }

    public void setSha(String sha) {
        this.mSha = sha;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getGitUrl() {
        return mGitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.mGitUrl = gitUrl;
    }

    public String getHtmlUrl() {
        return mHtmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.mHtmlUrl = htmlUrl;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.mDownloadUrl = downloadUrl;
    }

    public RepositoryReadmeLinks getLinks() {
        return mLinks;
    }

    public void setLinks(RepositoryReadmeLinks links) {
        this.mLinks = links;
    }
}
