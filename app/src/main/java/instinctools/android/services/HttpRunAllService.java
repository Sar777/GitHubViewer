package instinctools.android.services;

import android.app.IntentService;
import android.content.Intent;

import instinctools.android.services.http.notification.HttpGithubAllNotificationService;
import instinctools.android.services.http.notification.HttpGithubParticipatingNotificationService;
import instinctools.android.services.http.notification.HttpGithubUnreadNotificationService;
import instinctools.android.services.http.repository.HttpUpdateMyRepositoriesService;
import instinctools.android.services.http.repository.HttpUpdateStarsRepositoriesService;
import instinctools.android.services.http.repository.HttpUpdateWatchRepositoriesService;

public class HttpRunAllService extends IntentService {
    private static final String TAG = "HttpRunAllService";

    public HttpRunAllService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // My Repositories
        startService(new Intent(this, HttpUpdateMyRepositoriesService.class));
        // My watch repositories
        startService(new Intent(this, HttpUpdateWatchRepositoriesService.class));
        // My starts repositories
        startService(new Intent(this, HttpUpdateStarsRepositoriesService.class));

        // Notifications
        startService(new Intent(this, HttpGithubAllNotificationService.class));
        startService(new Intent(this, HttpGithubUnreadNotificationService.class));
        startService(new Intent(this, HttpGithubParticipatingNotificationService.class));
    }
}
