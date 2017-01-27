package instinctools.android.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import instinctools.android.activity.ProfileActivity;
import instinctools.android.models.github.user.User;
import instinctools.android.services.github.user.GithubServiceUser;

public class AsyncUserInfoLoader extends AsyncTaskLoader<User> {
    private String mUsername;

    public AsyncUserInfoLoader(Context context, Bundle bundle) {
        super(context);

        mUsername = bundle.getString(ProfileActivity.BUNDLE_USERNAME);
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
