package instinctools.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import instinctools.android.adapters.sync.RepositoriesSyncAdapter;

public class RepositoriesSyncService extends Service {
    private static RepositoriesSyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new RepositoriesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}