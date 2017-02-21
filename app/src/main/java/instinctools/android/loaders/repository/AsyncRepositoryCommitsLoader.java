package instinctools.android.loaders.repository;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import instinctools.android.models.github.commits.CommitsListResponse;
import instinctools.android.services.github.repository.GithubServiceRepository;

public class AsyncRepositoryCommitsLoader extends AsyncTaskLoader<CommitsListResponse> {
    private String mFullname;

    public AsyncRepositoryCommitsLoader(Context context, @Nullable String fullname) {
        super(context);

        this.mFullname = fullname;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public CommitsListResponse loadInBackground() {
        return GithubServiceRepository.getCommits(mFullname);
    }
}
