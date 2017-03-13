package instinctools.android.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import instinctools.android.R;
import instinctools.android.activity.ProfileActivity;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.adapters.repository.RepositoriesAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.listeners.EndlessRecyclerOnScrollListener;
import instinctools.android.loaders.user.AsyncUserRepositoriesListLoader;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.repositories.RepositoriesListResponse;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.user.GithubServiceUser;

public class ProfileRepositoriesFragment extends Fragment implements LoaderManager.LoaderCallbacks<RepositoriesListResponse>, SwipeRefreshLayout.OnRefreshListener {
    // View
    private ViewGroup mViewGroupContainer;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private AbstractRecyclerAdapter mRepositoriesAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RepositoriesListResponse mLastRepositoriesListResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_repositories, null);

        mViewGroupContainer = (ViewGroup) view.findViewById(R.id.layout_profile_repositories);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_profile_repositories);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_profile_repositories);
        mProgressBar.setVisibility(View.VISIBLE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_profile_repositories);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                if (mLastRepositoriesListResponse == null || mLastRepositoriesListResponse.getPageLinks().getNext() == null)
                    return;

                mRepositoriesAdapter.getResource().add(null);
                mRecyclerView.post(new Runnable() {
                    public void run() {
                        mRepositoriesAdapter.notifyItemInserted(mRepositoriesAdapter.getResource().size() - 1);
                    }
                });
                GithubServiceUser.getRepositorisListByUrl(mLastRepositoriesListResponse.getPageLinks().getNext(), new GithubServiceListener<RepositoriesListResponse>() {
                    @Override
                    public void onError(int code, @Nullable ErrorResponse response) {
                        mRepositoriesAdapter.getResource().remove(mRepositoriesAdapter.getResource().size() - 1);
                        mRepositoriesAdapter.notifyItemRemoved(mRepositoriesAdapter.getResource().size() - 1);
                        mLoading = false;
                    }

                    @Override
                    public void onSuccess(RepositoriesListResponse response) {
                        if (response != null && response.getRepositories() != null) {
                            int saveOldPos = mRepositoriesAdapter.getResource().size() - 1;
                            mRepositoriesAdapter.getResource().remove(saveOldPos);
                            mRepositoriesAdapter.getResource().addAll(response.getRepositories());
                            mRepositoriesAdapter.notifyItemRemoved(saveOldPos);
                            mRepositoriesAdapter.notifyItemRangeInserted(saveOldPos + 1, response.getRepositories().size());
                            mLastRepositoriesListResponse = response;
                        }
                        else
                            Snackbar.make(mViewGroupContainer, R.string.msg_profile_events_loading_fail, Snackbar.LENGTH_SHORT).show();

                        mLoading = false;
                    }
                });
            }
        });

        mRepositoriesAdapter = new RepositoriesAdapter(getContext(), true);
        mRecyclerView.setAdapter(mRepositoriesAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, false));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getSupportLoaderManager().initLoader(ProfileActivity.LOADER_REPOSITORIES_ID, Bundle.EMPTY, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSupportLoaderManager().destroyLoader(ProfileActivity.LOADER_REPOSITORIES_ID);
    }

    @Override
    public Loader<RepositoriesListResponse> onCreateLoader(int id, Bundle args) {
        return new AsyncUserRepositoriesListLoader(getContext(), ((ProfileActivity)getActivity()).getUserName());
    }

    @Override
    public void onLoadFinished(Loader<RepositoriesListResponse> loader, RepositoriesListResponse response) {
        if (response != null && response.getRepositories() != null)
            mRepositoriesAdapter.setResource(response.getRepositories());
        else
            Snackbar.make(mViewGroupContainer, R.string.msg_profile_events_loading_fail, Snackbar.LENGTH_SHORT).show();

        mRepositoriesAdapter.notifyDataSetChanged();

        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

        mLastRepositoriesListResponse = response;
    }

    @Override
    public void onLoaderReset(Loader<RepositoriesListResponse> loader) {

    }

    @Override
    public void onRefresh() {
        getActivity().getSupportLoaderManager().getLoader(ProfileActivity.LOADER_REPOSITORIES_ID).forceLoad();
    }
}
