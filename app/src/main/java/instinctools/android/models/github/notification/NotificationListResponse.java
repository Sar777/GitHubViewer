package instinctools.android.models.github.notification;

import java.util.List;

import instinctools.android.models.github.PageLinks;

public class NotificationListResponse {
    private PageLinks mPageLinks;
    private List<Notification> mNotifications;

    public NotificationListResponse(List<Notification> notifications) {
        this.mNotifications = notifications;
    }

    public List<Notification> getNotifications() {
        return mNotifications;
    }

    public PageLinks getPageLinks() {
        return mPageLinks;
    }

    public void setPageLinks(PageLinks pageLinks) {
        this.mPageLinks = pageLinks;
    }
}
