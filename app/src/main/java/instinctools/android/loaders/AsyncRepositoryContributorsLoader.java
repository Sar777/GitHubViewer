package instinctools.android.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import instinctools.android.models.github.contributors.ContributorsListResponse;
import instinctools.android.services.github.repository.GithubServiceRepository;

public class AsyncRepositoryContributorsLoader extends AsyncTaskLoader<ContributorsListResponse> {
    private String mFullName;

    public AsyncRepositoryContributorsLoader(Context context, String fullname) {
        super(context);

        mFullName = fullname;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ContributorsListResponse loadInBackground() {
        return GithubServiceRepository.getContributors(mFullName);
    }
}
