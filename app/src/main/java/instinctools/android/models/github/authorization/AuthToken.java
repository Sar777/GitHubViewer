package instinctools.android.models.github.authorization;

import java.util.Set;

/**
 * Created by orion on 12.1.17.
 */

public class AuthToken {
    private int mId;
    private String mUrl;
    private Set<String> mScopes;
    private String mToken;
    private String mTokenLastEight;
    private String mHashedToken;
    private AppToken mAppToken;
    private String mNote;
    private String mNoteUrl;
    private String mUpdatedAt;
    private String mCreatedAt;
    private String mFingerprint;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public Set<String> getScopes() {
        return mScopes;
    }

    public void setScopes(Set<String> scopes) {
        this.mScopes = scopes;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    public String getTokenLastEight() {
        return mTokenLastEight;
    }

    public void setTokenLastEight(String tokenLastEight) {
        this.mTokenLastEight = tokenLastEight;
    }

    public String getHashedToken() {
        return mHashedToken;
    }

    public void setHashedToken(String hashedToken) {
        this.mHashedToken = hashedToken;
    }

    public AppToken getAppToken() {
        return mAppToken;
    }

    public void setAppToken(AppToken appToken) {
        this.mAppToken = appToken;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        this.mNote = note;
    }

    public String getNoteUrl() {
        return mNoteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.mNoteUrl = noteUrl;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.mUpdatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.mCreatedAt = createdAt;
    }

    public String getFingerprint() {
        return mFingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.mFingerprint = fingerprint;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "mId=" + mId +
                ", mUrl='" + mUrl + '\'' +
                ", mScopes=" + mScopes +
                ", mToken='" + mToken + '\'' +
                ", mTokenLastEight=" + mTokenLastEight +
                ", mHashedToken='" + mHashedToken + '\'' +
                ", mAppToken=" + mAppToken +
                ", mNote='" + mNote + '\'' +
                ", mNoteUrl='" + mNoteUrl + '\'' +
                ", mUpdatedAt='" + mUpdatedAt + '\'' +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mFingerprint='" + mFingerprint + '\'' +
                '}';
    }
}
