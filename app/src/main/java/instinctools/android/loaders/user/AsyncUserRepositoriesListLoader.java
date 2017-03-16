package instinctools.android.loaders.user;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import instinctools.android.models.github.repositories.RepositoriesListResponse;
import instinctools.android.models.github.user.User;
import instinctools.android.services.github.user.GithubServiceUser;

public class AsyncUserRepositoriesListLoader extends AsyncTaskLoader<RepositoriesListResponse> {
    private String mUsername;

    public AsyncUserRepositoriesListLoader(Context context, String username) {
        super(context);

        mUsername = username;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public RepositoriesListResponse loadInBackground() {
        if (TextUtils.isEmpty(mUsername)) {
            User user = GithubServiceUser.getCurrentUser();
            if (user == null)
                return null;

            mUsername = user.getLogin();
        }

        return GithubServiceUser.getRepositoriesListResponse(mUsername);
    }
}