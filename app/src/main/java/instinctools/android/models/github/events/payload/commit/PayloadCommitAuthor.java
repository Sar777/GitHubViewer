package instinctools.android.models.github.events.payload.commit;

public class PayloadCommitAuthor {
    private String mEmail;
    private String mName;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
