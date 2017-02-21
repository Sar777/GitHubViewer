package instinctools.android.models.github.notification;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import instinctools.android.models.github.PageLinks;
import instinctools.android.services.github.GitHubResponse;

public class NotificationListResponse extends GitHubResponse implements Parcelable {
    private List<Notification> mNotifications;

    public NotificationListResponse(List<Notification> notifications) {
        this.mNotifications = notifications;
    }

    private NotificationListResponse(Parcel in) {
        setPageLinks((PageLinks)in.readParcelable(PageLinks.class.getClassLoader()));
        mNotifications = in.createTypedArrayList(Notification.CREATOR);
    }

    public List<Notification> getNotifications() {
        return mNotifications;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getPageLinks(), flags);
        dest.writeTypedList(mNotifications);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationListResponse> CREATOR = new Creator<NotificationListResponse>() {
        @Override
        public NotificationListResponse createFromParcel(Parcel in) {
            return new NotificationListResponse(in);
        }

        @Override
        public NotificationListResponse[] newArray(int size) {
            return new NotificationListResponse[size];
        }
    };
}
