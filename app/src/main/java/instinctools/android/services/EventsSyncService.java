package instinctools.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import instinctools.android.adapters.sync.EventsSyncAdapter;

public class EventsSyncService extends Service {
    private static EventsSyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new EventsSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}