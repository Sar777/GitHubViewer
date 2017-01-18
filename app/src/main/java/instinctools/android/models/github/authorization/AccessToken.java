package instinctools.android.models.github.authorization;

/**
 * Created by orion on 12.1.17.
 */

public class AccessToken {
    private String mAccessToken;
    private String mTokenType;
    private String mScopes;

    public String getAcessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String acessToken) {
        this.mAccessToken = acessToken;
    }

    public String getTokenType() {
        return mTokenType;
    }

    public void setTokenType(String tokenType) {
        this.mTokenType = tokenType;
    }

    public String getScopes() {
        return mScopes;
    }

    public void setScopes(String scopes) {
        this.mScopes = scopes;
    }
}
