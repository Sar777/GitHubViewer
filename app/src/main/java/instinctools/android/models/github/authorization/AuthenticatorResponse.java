package instinctools.android.models.github.authorization;

import instinctools.android.models.github.user.User;

public class AuthenticatorResponse {
    private final AccessToken mAccessToken;
    private User mUser;

    public AuthenticatorResponse(AccessToken accessToken) {
        this.mAccessToken = accessToken;
    }

    public AccessToken getAccessToken() {
        return mAccessToken;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }
}
