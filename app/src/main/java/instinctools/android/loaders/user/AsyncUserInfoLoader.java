package instinctools.android.loaders.user;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import instinctools.android.models.github.user.User;
import instinctools.android.services.github.user.GithubServiceUser;

public class AsyncUserInfoLoader extends AsyncTaskLoader<User> {
    private String mUsername;

    public AsyncUserInfoLoader(Context context, String username) {
        super(context);

        this.mUsername = username;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public User loadInBackground() {
        User user;
        if (!TextUtils.isEmpty(mUsername))
            user = GithubServiceUser.getUser(mUsername);
        else
            user = GithubServiceUser.getCurrentUser();

        return user;
    }
}
