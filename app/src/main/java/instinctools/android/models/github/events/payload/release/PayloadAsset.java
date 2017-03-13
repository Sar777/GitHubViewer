package instinctools.android.models.github.events.payload.release;

import instinctools.android.models.github.user.UserShort;

public class PayloadAsset {
    private Integer mId;
    private String mUrl;
    private String mName;
    private String mLabel;
    private UserShort mUploader;
    private Integer mSize;
    private Integer mDownloadCount;
    private String mBrowserDownloadUrl;

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

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public UserShort getUploader() {
        return mUploader;
    }

    public void setUploader(UserShort uploader) {
        this.mUploader = uploader;
    }

    public Integer getSize() {
        return mSize;
    }

    public void setSize(Integer size) {
        this.mSize = size;
    }

    public Integer getDownloadCount() {
        return mDownloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.mDownloadCount = downloadCount;
    }

    public String getBrowserDownloadUrl() {
        return mBrowserDownloadUrl;
    }

    public void setBrowserDownloadUrl(String browserDownloadUrl) {
        this.mBrowserDownloadUrl = browserDownloadUrl;
    }
}
