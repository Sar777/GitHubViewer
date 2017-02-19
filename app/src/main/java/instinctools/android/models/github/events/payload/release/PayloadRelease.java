package instinctools.android.models.github.events.payload.release;

import java.util.List;

import instinctools.android.models.github.user.UserShort;

public class PayloadRelease {
    private Integer mId;
    private String mUrl;
    private String mHtmlUrl;
    private String mTagName;
    private String mName;
    private UserShort mAuthor;
    private List<PayloadAsset> mAssets;

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

    public String getTagName() {
        return mTagName;
    }

    public void setTagName(String tagName) {
        this.mTagName = tagName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public UserShort getmAuthor() {
        return mAuthor;
    }

    public void setAuthor(UserShort author) {
        this.mAuthor = author;
    }

    public List<PayloadAsset> getAssets() {
        return mAssets;
    }

    public void setAssets(List<PayloadAsset> assets) {
        this.mAssets = assets;
    }
}
