package instinctools.android.services;

import android.content.Intent;

import java.util.List;

import instinctools.android.constans.Constants;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.services.github.user.GithubServiceUser;

public class HttpUpdateWatchRepositoryService extends HttpRepositoryService {
    public HttpUpdateWatchRepositoryService() {
        this.mTypeInfo = Constants.REPOSITORY_TYPE_WATCH;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Repository> repositories = GithubServiceUser.getWatchRepositoryList();
        if (repositories == null)
            return;

        onHandleIntent(repositories);
    }
}