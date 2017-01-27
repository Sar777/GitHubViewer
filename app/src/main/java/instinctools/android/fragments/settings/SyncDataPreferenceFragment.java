package instinctools.android.fragments.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;

import instinctools.android.R;
import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.constans.Constants;
import instinctools.android.storages.SettingsStorage;
import instinctools.android.utility.Services;

public class SyncDataPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_sync_data);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (sharedPreferences == null)
            return;

        int interval = Integer.parseInt(sharedPreferences.getString(key, String.valueOf(Constants.INTERVAL_UPDATE_REPO_SERVICES)));

        if (key.contains(getString(R.string.title_pref_key_sync_my_repo))) {
            Services.rescheduleAlarmBroadcast(getActivity().getApplicationContext(),
                    OnAlarmReceiver.class,
                    OnAlarmReceiver.REQUEST_MY_REPO_CODE,
                    interval * 60 * 1000);

        } else if (key.contains(getString(R.string.title_pref_key_sync_watch_repo))) {
            Services.rescheduleAlarmBroadcast(getActivity().getApplicationContext(),
                    OnAlarmReceiver.class,
                    OnAlarmReceiver.REQUEST_WATCH_REPO_CODE,
                    SettingsStorage.getIntervalUpdateWatchesRepo() * 60 * 1000);

        } else if (key.contains(getString(R.string.title_pref_key_sync_stars_repo))) {
            Services.rescheduleAlarmBroadcast(getActivity().getApplicationContext(),
                    OnAlarmReceiver.class,
                    OnAlarmReceiver.REQUEST_STARS_REPO_CODE,
                    interval * 60 * 1000);
        } else if (key.contains(getString(R.string.title_pref_key_sync_notifications))) {
            Services.rescheduleAlarmBroadcast(getActivity().getApplicationContext(),
                    OnAlarmReceiver.class,
                    OnAlarmReceiver.REQUEST_GITHUB_NOTIFICATIONS_UNREAD,
                    interval * 60 * 1000);
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