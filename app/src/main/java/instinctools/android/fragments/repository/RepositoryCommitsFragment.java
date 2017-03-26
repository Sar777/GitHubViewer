package instinctools.android.fragments.repository;

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

import java.util.Collections;

import instinctools.android.R;
import instinctools.android.activity.RepositoryDescriptionActivity;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.adapters.commits.CommitsAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.listeners.EndlessRecyclerOnScrollListener;
import instinctools.android.loaders.repository.AsyncRepositoryCommitsLoader;
import instinctools.android.models.github.commits.CommitsListResponse;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.repository.GithubServiceRepository;

public class RepositoryCommitsFragment extends Fragment implements LoaderManager.LoaderCallbacks<CommitsListResponse>, SwipeRefreshLayout.OnRefreshListener {
    // View
    private ViewGroup mViewGroupContainer;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private AbstractRecyclerAdapter mCommitsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private CommitsListResponse mLastCommitsListResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repository_description_commits, null);

        mViewGroupContainer = (ViewGroup) view.findViewById(R.id.layout_repository_description_commits);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_repository_commits);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_repository_commits);
        mProgressBar.setVisibility(View.VISIBLE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_repository_commits);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                if (mLastCommitsListResponse == null || mLastCommitsListResponse.getPageLinks().getNext() == null)
                    return;

                mCommitsAdapter.getResource().add(null);
                mRecyclerView.post(new Runnable() {
                    public void run() {
                        mCommitsAdapter.notifyItemInserted(mCommitsAdapter.getResource().size() - 1);
                    }
                });
                GithubServiceRepository.getCommitsByUrl(mLastCommitsListResponse.getPageLinks().getNext(), new GithubServiceListener<CommitsListResponse>() {
                    @Override
                    public void onError(int code, @Nullable ErrorResponse response) {
                        mCommitsAdapter.getResource().remove(mCommitsAdapter.getResource().size() - 1);
                        mCommitsAdapter.notifyItemRemoved(mCommitsAdapter.getResource().size() - 1);
                        mLoading = false;
                    }

                    @Override
                    public void onSuccess(CommitsListResponse response) {
                        if (response != null && response.getCommits() != null) {
                            int saveOldPos = mCommitsAdapter.getResource().size() - 1;
                            mCommitsAdapter.getResource().remove(saveOldPos);
                            mCommitsAdapter.getResource().addAll(response.getCommits());
                            mCommitsAdapter.notifyItemRemoved(saveOldPos);
                            mCommitsAdapter.notifyItemRangeInserted(saveOldPos + 1, response.getCommits().size());
                            mLastCommitsListResponse = response;
                        } else
                            Snackbar.make(mViewGroupContainer, R.string.msg_repository_description_commits_loading_fail, Snackbar.LENGTH_SHORT).show();

                        mLoading = false;
                    }
                });
            }
        });

        mCommitsAdapter = new CommitsAdapter(getContext());
        mRecyclerView.setAdapter(mCommitsAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, false));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(RepositoryDescriptionActivity.LOADER_REPOSITORY_COMMITS_ID, Bundle.EMPTY, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSupportLoaderManager().destroyLoader(RepositoryDescriptionActivity.LOADER_REPOSITORY_COMMITS_ID);
    }

    @Override
    public Loader<CommitsListResponse> onCreateLoader(int id, Bundle args) {
        return new AsyncRepositoryCommitsLoader(getContext(), ((RepositoryDescriptionActivity)getActivity()).getFullName());
    }

    @Override
    public void onLoadFinished(Loader<CommitsListResponse> loader, CommitsListResponse response) {
        if (response != null && response.getCommits() != null)
            mCommitsAdapter.setResource(response.getCommits());
        else
            Snackbar.make(mViewGroupContainer, R.string.msg_repository_description_commits_loading_fail, Snackbar.LENGTH_SHORT).show();

        mCommitsAdapter.notifyDataSetChanged();

        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

        mLastCommitsListResponse = response;
    }

    @Override
    public void onLoaderReset(Loader<CommitsListResponse> loader) {

    }

    @Override
    public void onRefresh() {
        getActivity().getSupportLoaderManager().getLoader(RepositoryDescriptionActivity.LOADER_REPOSITORY_COMMITS_ID).forceLoad();
    }
}
