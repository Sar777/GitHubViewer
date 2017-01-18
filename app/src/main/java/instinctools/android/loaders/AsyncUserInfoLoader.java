package instinctools.android.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import instinctools.android.models.github.user.User;
import instinctools.android.services.github.GithubServices;

public class AsyncUserInfoLoader extends AsyncTaskLoader<User> {
    public AsyncUserInfoLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public User loadInBackground() {
        return GithubServices.getUser();
    }
}
