package instinctools.android.account;

import android.accounts.Account;
import android.os.Parcel;

public class GitHubAccount extends Account {

    public static final String TYPE = "instinctools.android";

    public static final String TOKEN_FULL_ACCESS = "instinctools.android.TOKEN_FULL_ACCESS";

    public static final String KEY_PASSWORD = "instinctools.android.KEY_PASSWORD";

    public GitHubAccount(Parcel in) {
        super(in);
    }

    public GitHubAccount(String name) {
        super(name, TYPE);
    }

}