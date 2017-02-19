package instinctools.android.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import instinctools.android.activity.RepositoryDescriptionActivity;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.services.github.repository.GithubServiceRepository;

public class AsyncRepositoryInfoLoader extends AsyncTaskLoader<Repository> {
    private String mFullName;

    public AsyncRepositoryInfoLoader(Context context, Bundle bundle) {
        super(context);

        mFullName = bundle.getString(RepositoryDescriptionActivity.BUNDLE_REPOSITORY_NAME);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Repository loadInBackground() {
        return GithubServiceRepository.getRepository(mFullName);
    }
}
