package instinctools.android.loaders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import instinctools.android.models.github.events.EventsListResponse;
import instinctools.android.models.github.user.User;
import instinctools.android.services.github.events.GithubServiceEvents;
import instinctools.android.services.github.user.GithubServiceUser;

public class AsyncReceivedEventsLoader extends AsyncTaskLoader<EventsListResponse> {
    private String mUsername;

    public AsyncReceivedEventsLoader(Context context, @Nullable String username) {
        super(context);

        this.mUsername = username;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public EventsListResponse loadInBackground() {
        if (TextUtils.isEmpty(mUsername)) {
            User user = GithubServiceUser.getCurrentUser();
            if (user == null)
                return null;

            mUsername = user.getLogin();
        }

        return GithubServiceEvents.getReceivedEventsResponse(mUsername);
    }
}
