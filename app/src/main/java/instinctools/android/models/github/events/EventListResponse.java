package instinctools.android.models.github.events;

import java.util.List;

import instinctools.android.models.github.PageLinks;

public class EventListResponse {
    private List<Event> mEvents;
    private PageLinks mPageLinks;

    public EventListResponse(List<Event> events) {
        this.mEvents = events;
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public PageLinks getPageLinks() {
        return mPageLinks;
    }

    public void setPageLinks(PageLinks pageLinks) {
        this.mPageLinks = pageLinks;
    }
}
