package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import instinctools.android.R;
import instinctools.android.adapters.contributors.ContributorsAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.listeners.EndlessRecyclerOnScrollListener;
import instinctools.android.loaders.repository.AsyncRepositoryContributorsLoader;
import instinctools.android.models.github.contributors.ContributorsListResponse;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.repository.GithubServiceRepository;

public class RepositoryContributorsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ContributorsListResponse> {
    private static final int LOADER_CONTRIBUTORS_ID = 1;

    // View
    private RecyclerView mRecyclerView;
    private ContributorsAdapter mContributorsAdapter;
    private ProgressBar mProgressBar;

    private ContributorsListResponse mLastContributorsListResponse;

    private String mFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_contributors);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        mFullName = intent.getStringExtra(RepositoryDescriptionActivity.EXTRA_REPOSITORY_FULLNAME);
        if (TextUtils.isEmpty(mFullName)) {
            finish();
            return;
        }

        initView();

        getSupportLoaderManager().initLoader(LOADER_CONTRIBUTORS_ID, Bundle.EMPTY, this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle(mFullName);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_repository_contributors_list);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_repository_contributors);
        mProgressBar.setVisibility(View.VISIBLE);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, false));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                if (mLastContributorsListResponse == null || mLastContributorsListResponse.getPageLinks().getNext() == null)
                    return;

                mContributorsAdapter.getResource().add(null);
                mRecyclerView.post(new Runnable() {
                    public void run() {
                        mContributorsAdapter.notifyItemInserted(mContributorsAdapter.getResource().size() - 1);
                    }
                });
                GithubServiceRepository.getContributionsByUrl(mLastContributorsListResponse.getPageLinks().getNext(), new GithubServiceListener<ContributorsListResponse>() {
                    @Override
                    public void onError(int code, @Nullable ErrorResponse response) {
                        mContributorsAdapter.getResource().remove(mContributorsAdapter.getResource().size() - 1);
                        mContributorsAdapter.notifyItemRemoved(mContributorsAdapter.getResource().size() - 1);
                        mLoading = false;
                    }

                    @Override
                    public void onSuccess(ContributorsListResponse response) {
                        if (response != null) {
                            int saveOldPos = mContributorsAdapter.getResource().size() - 1;
                            mContributorsAdapter.getResource().remove(saveOldPos);
                            mContributorsAdapter.getResource().addAll(response.getUsers());
                            mContributorsAdapter.notifyItemRemoved(saveOldPos);
                            mContributorsAdapter.notifyItemRangeInserted(saveOldPos + 1, response.getUsers().size());
                            mLastContributorsListResponse = response;
                        }

                        mLoading = false;
                    }
                });
            }
        });

        mContributorsAdapter = new ContributorsAdapter(this);
        mRecyclerView.setAdapter(mContributorsAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public Loader<ContributorsListResponse> onCreateLoader(int id, Bundle args) {
        return new AsyncRepositoryContributorsLoader(this, mFullName);
    }

    @Override
    public void onLoadFinished(Loader<ContributorsListResponse> loader, ContributorsListResponse response) {
        mContributorsAdapter.setResource(response.getUsers());
        mContributorsAdapter.notifyDataSetChanged();

        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        mLastContributorsListResponse = response;
    }

    @Override
    public void onLoaderReset(Loader<ContributorsListResponse> loader) {

    }
}
