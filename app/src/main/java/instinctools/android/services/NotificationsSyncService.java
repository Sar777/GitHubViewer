package instinctools.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import instinctools.android.adapters.sync.NotificationsSyncAdapter;

public class NotificationsSyncService extends Service {
    private static NotificationsSyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new NotificationsSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}