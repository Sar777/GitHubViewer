package instinctools.android.loaders.repository;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import instinctools.android.fragments.issues.RepositoryIssueFragment;
import instinctools.android.models.github.issues.IssueListResponse;
import instinctools.android.models.github.issues.IssueState;
import instinctools.android.services.github.Direction;
import instinctools.android.services.github.repository.GithubServiceRepository;

public class AsyncRepositoryIssuesLoader extends AsyncTaskLoader<IssueListResponse> {
    private String mFullname;
    private IssueState mState;

    public AsyncRepositoryIssuesLoader(Context context, Bundle args) {
        super(context);

        this.mFullname = args.getString(RepositoryIssueFragment.BUNDLE_FULLNAME);
        this.mState = args.getParcelable(RepositoryIssueFragment.BUNDLE_STATE);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public IssueListResponse loadInBackground() {
        return GithubServiceRepository.getIssues(mFullname, mState, Direction.DESC);
    }
}
