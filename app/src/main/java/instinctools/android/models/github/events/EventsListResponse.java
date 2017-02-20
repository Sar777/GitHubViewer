package instinctools.android.models.github.events;

import java.util.List;

import instinctools.android.services.github.GitHubResponse;

public class EventsListResponse extends GitHubResponse {
    private List<Event> mEvents;

    public EventsListResponse(List<Event> events) {
        this.mEvents = events;
    }

    public List<Event> getEvents() {
        return mEvents;
    }
}
