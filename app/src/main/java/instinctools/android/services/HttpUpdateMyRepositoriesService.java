package instinctools.android.services;

import android.content.Intent;

import java.util.List;

import instinctools.android.constans.Constants;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.services.github.repository.GithubServiceRepository;

public class HttpUpdateMyRepositoriesService extends HttpRepositoryService {
    public HttpUpdateMyRepositoriesService() {
        this.mTypeInfo = Constants.REPOSITORY_TYPE_MY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Repository> repositories = GithubServiceRepository.getRepositoryList();
        if (repositories == null)
            return;

        onHandleIntent(repositories);
    }
}
