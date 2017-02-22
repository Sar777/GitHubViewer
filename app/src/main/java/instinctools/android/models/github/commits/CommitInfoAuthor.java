package instinctools.android.models.github.commits;

import java.util.Date;

public class CommitInfoAuthor {
    private String mName;
    private String mEmail;
    private Date mDate;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }
}
