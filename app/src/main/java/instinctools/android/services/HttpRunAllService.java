package instinctools.android.services;

import android.app.IntentService;
import android.content.Intent;

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
    }
}
