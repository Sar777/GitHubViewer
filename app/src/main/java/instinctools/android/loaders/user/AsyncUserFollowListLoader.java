package instinctools.android.loaders.user;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import instinctools.android.models.github.follow.FollowListResponse;
import instinctools.android.models.github.follow.FollowType;
import instinctools.android.models.github.user.User;
import instinctools.android.services.github.user.GithubServiceUser;

public class AsyncUserFollowListLoader extends AsyncTaskLoader<FollowListResponse> {
    private String mUsername;
    private FollowType mFollowType;

    public AsyncUserFollowListLoader(Context context, String username, FollowType followType) {
        super(context);

        mUsername = username;
        mFollowType = followType;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public FollowListResponse loadInBackground() {
        if (TextUtils.isEmpty(mUsername)) {
            User user = GithubServiceUser.getCurrentUser();
            if (user == null)
                return null;

            mUsername = user.getLogin();
        }

        return GithubServiceUser.getFollowListResponse(mUsername, mFollowType);
    }
}