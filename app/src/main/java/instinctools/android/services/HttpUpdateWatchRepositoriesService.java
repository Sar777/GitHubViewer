package instinctools.android.services;

import android.content.Intent;

import java.util.List;

import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.constans.Constants;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.services.github.user.GithubServiceUser;
import instinctools.android.storages.SettingsStorage;
import instinctools.android.utility.Services;

public class HttpUpdateWatchRepositoriesService extends HttpRepositoryService {
    public HttpUpdateWatchRepositoriesService() {
        this.mTypeInfo = Constants.REPOSITORY_TYPE_WATCH;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Services.rescheduleAlarmBroadcast(this,
                OnAlarmReceiver.class,
                OnAlarmReceiver.REQUEST_WATCH_REPO_CODE,
                SettingsStorage.getIntervalUpdateWatchesRepo() * 60 * 1000);

        List<Repository> repositories = GithubServiceUser.getWatchRepositoryList();
        if (repositories == null)
            return;

        onHandleIntent(repositories);
    }
}
