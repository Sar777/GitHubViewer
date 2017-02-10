package instinctools.android.fragments.settings;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.constans.Constants;
import instinctools.android.database.providers.NotificationsProvider;
import instinctools.android.database.providers.RepositoriesProvider;

public class SyncDataPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_sync_data);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (sharedPreferences == null)
            return;

        if (key.contains(getString(R.string.title_pref_key_sync_repositories))) {
            int interval = Integer.parseInt(sharedPreferences.getString(key, String.valueOf(Constants.INTERVAL_UPDATE_REPO_SERVICES)));
            ContentResolver.addPeriodicSync(App.getApplicationAccount(), RepositoriesProvider.AUTHORITY, Bundle.EMPTY, interval * 60);
        } else if (key.contains(getString(R.string.title_pref_key_sync_notifications))) {
            int interval = Integer.parseInt(sharedPreferences.getString(key, String.valueOf(Constants.INTERVAL_UPDATE_NOTIFICATIONS)));
            ContentResolver.addPeriodicSync(App.getApplicationAccount(), NotificationsProvider.AUTHORITY, Bundle.EMPTY, interval * 60);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_settings) + " - " + getString(R.string.title_pref_header_data_sync));

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}