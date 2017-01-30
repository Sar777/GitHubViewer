package instinctools.android.models.github.authorization;

public class AppToken {
    private String mUrl;
    private String mName;
    private String mClientId;

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

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String clientId) {
        this.mClientId = clientId;
    }

    @Override
    public String toString() {
        return "AppToken{" +
                "mUrl='" + mUrl + '\'' +
                ", mName='" + mName + '\'' +
                ", mClientId='" + mClientId + '\'' +
                '}';
    }
}
