package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import instinctools.android.R;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.adapters.follow.FollowsAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.fragments.profile.ProfileAboutFragment;
import instinctools.android.listeners.EndlessRecyclerOnScrollListener;
import instinctools.android.loaders.user.AsyncUserFollowListLoader;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.follow.FollowListResponse;
import instinctools.android.models.github.follow.FollowType;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.user.GithubServiceUser;

public class FollowActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<FollowListResponse> {
    private static int LOADER_FOLLOW_ID = 1;

    // View
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private String mUserName;
    private FollowType mFollowType = FollowType.Followers;
    private AbstractRecyclerAdapter mFollowAdapter;

    private FollowListResponse mLastFollowListResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        initView();

        getSupportLoaderManager().initLoader(LOADER_FOLLOW_ID, Bundle.EMPTY, this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            mFollowType = (FollowType) intent.getSerializableExtra(ProfileAboutFragment.EXTRA_FOLLOW_TYPE);
            mUserName = intent.getStringExtra(ProfileAboutFragment.EXTRA_USERNAME);
        }

        toolbar.setTitle(getString(mFollowType == FollowType.Followers ? R.string.title_profile_followers : R.string.title_profile_following));

        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_follow);
        mRecyclerView.setVisibility(View.INVISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                if (mLastFollowListResponse == null || mLastFollowListResponse.getPageLinks().getNext() == null)
                    return;

                mFollowAdapter.getResource().add(null);
                mRecyclerView.post(new Runnable() {
                    public void run() {
                        mFollowAdapter.notifyItemInserted(mFollowAdapter.getResource().size() - 1);
                    }
                });
                GithubServiceUser.getFollowListByUrl(mLastFollowListResponse.getPageLinks().getNext(), new GithubServiceListener<FollowListResponse>() {
                    @Override
                    public void onError(int code, @Nullable ErrorResponse response) {
                        mFollowAdapter.getResource().remove(mFollowAdapter.getResource().size() - 1);
                        mFollowAdapter.notifyItemRemoved(mFollowAdapter.getResource().size() - 1);
                        mLoading = false;
                    }

                    @Override
                    public void onSuccess(FollowListResponse response) {
                        if (response != null && response.getUsers() != null) {
                            int saveOldPos = mFollowAdapter.getResource().size() - 1;
                            mFollowAdapter.getResource().remove(saveOldPos);
                            mFollowAdapter.getResource().addAll(response.getUsers());
                            mFollowAdapter.notifyItemRemoved(saveOldPos);
                            mFollowAdapter.notifyItemRangeInserted(saveOldPos + 1, response.getUsers().size());
                            mLastFollowListResponse = response;
                        }
                        else
                            Snackbar.make(findViewById(R.id.activity_follow), R.string.msg_profile_events_loading_fail, Snackbar.LENGTH_SHORT).show();

                        mLoading = false;
                    }
                });
            }
        });

        mFollowAdapter = new FollowsAdapter(this);
        mRecyclerView.setAdapter(mFollowAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, false));

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_user_follow);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public Loader<FollowListResponse> onCreateLoader(int id, Bundle args) {
        return new AsyncUserFollowListLoader(this, mUserName, mFollowType);
    }

    @Override
    public void onLoadFinished(Loader<FollowListResponse> loader, FollowListResponse response) {
        if (response == null) {
            finish();
            return;
        }

        mFollowAdapter.setResource(response.getUsers());
        mFollowAdapter.notifyDataSetChanged();

        mLastFollowListResponse = response;

        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<FollowListResponse> loader) {

    }
}
