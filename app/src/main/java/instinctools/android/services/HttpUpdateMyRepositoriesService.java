package instinctools.android.services;

import android.content.Intent;

import java.util.List;

import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.constans.Constants;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.services.github.user.GithubServiceUser;
import instinctools.android.storages.SettingsStorage;
import instinctools.android.utility.Services;

public class HttpUpdateMyRepositoriesService extends HttpRepositoryService {
    public HttpUpdateMyRepositoriesService() {
        this.mTypeInfo = Constants.REPOSITORY_TYPE_MY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Services.scheduleAlarmBroadcast(this,
                OnAlarmReceiver.class,
                OnAlarmReceiver.REQUEST_MY_REPO_CODE,
                SettingsStorage.getIntervalUpdateMyRepo() * 60 * 1000);

        List<Repository> repositories = GithubServiceUser.getMyRepositoryList();
        if (repositories == null)
            return;

        onHandleIntent(repositories);
    }
}
