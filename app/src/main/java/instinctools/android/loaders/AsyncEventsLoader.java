package instinctools.android.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import instinctools.android.models.github.events.EventsListResponse;
import instinctools.android.services.github.events.GithubServiceEvents;

public class AsyncEventsLoader extends AsyncTaskLoader<EventsListResponse> {
    public AsyncEventsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public EventsListResponse loadInBackground() {
        return GithubServiceEvents.getReceivedEventsResponse("Sar777");
    }
}
