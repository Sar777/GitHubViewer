package instinctools.android.fragments.issues;

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
import instinctools.android.activity.RepositoryIssuesActivity;
import instinctools.android.adapters.issues.IssuesAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.listeners.EndlessRecyclerOnScrollListener;
import instinctools.android.loaders.repository.AsyncRepositoryIssuesLoader;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.issues.IssueListResponse;
import instinctools.android.models.github.issues.IssueState;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.repository.GithubServiceRepository;

public class RepositoryIssueFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<IssueListResponse> {
    private static final int LOADER_ISSUES_CLOSED = 1;
    private static final int LOADER_ISSUES_OPENED = 2;

    public static final String ARGUMENT_PAGE_TYPE = "PAGE_TYPE";

    public static final String BUNDLE_FULLNAME = "FULLNAME";
    public static final String BUNDLE_STATE = "STATE";

    private IssueState mState;
    private IssuesAdapter mIssuesAdapter;

    // View
    private ViewGroup mViewGroupContainer;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private IssueListResponse mLastIssuesListResponse;

    public RepositoryIssueFragment() {
        this.mState = IssueState.CLOSED;
    }

    public static RepositoryIssueFragment newInstance(IssueState state) {
        RepositoryIssueFragment fragment = new RepositoryIssueFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_TYPE, state.ordinal());
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            mState = IssueState.values()[getArguments().getInt(ARGUMENT_PAGE_TYPE)];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repository_issue, null);

        mViewGroupContainer = (ViewGroup) view.findViewById(R.id.layout_repository_issue);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_issues_list);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mIssuesAdapter = new IssuesAdapter(getContext());
        mRecyclerView.setAdapter(mIssuesAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                if (mLastIssuesListResponse == null || mLastIssuesListResponse.getPageLinks().getNext() == null)
                    return;

                mIssuesAdapter.getResource().add(null);
                mRecyclerView.post(new Runnable() {
                    public void run() {
                        mIssuesAdapter.notifyItemInserted(mIssuesAdapter.getResource().size() - 1);
                    }
                });
                GithubServiceRepository.getIssuesByUrl(mLastIssuesListResponse.getPageLinks().getNext(), new GithubServiceListener<IssueListResponse>() {
                    @Override
                    public void onError(int code, @Nullable ErrorResponse response) {
                        mIssuesAdapter.getResource().remove(mIssuesAdapter.getResource().size() - 1);
                        mIssuesAdapter.notifyItemRemoved(mIssuesAdapter.getResource().size() - 1);
                        mLoading = false;
                    }

                    @Override
                    public void onSuccess(IssueListResponse response) {
                        if (response != null && response.getIssues() != null) {
                            int saveOldPos = mIssuesAdapter.getResource().size() - 1;
                            mIssuesAdapter.getResource().remove(saveOldPos);
                            mIssuesAdapter.getResource().addAll(response.getIssues());
                            mIssuesAdapter.notifyItemRemoved(saveOldPos);
                            mIssuesAdapter.notifyItemRangeInserted(saveOldPos + 1, response.getIssues().size());
                            mLastIssuesListResponse = response;
                        }
                        else
                            Snackbar.make(mViewGroupContainer, R.string.msg_repositoy_issues_loading_fail, Snackbar.LENGTH_SHORT).show();

                        mLoading = false;
                    }
                });
            }
        });

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, false));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_issues);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_issues_loading);

        return view;
    }

    @Override
    public void onRefresh() {
        getActivity().getSupportLoaderManager().getLoader(getLoaderId()).forceLoad();
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_FULLNAME, ((RepositoryIssuesActivity)getActivity()).getFullName());
        bundle.putParcelable(BUNDLE_STATE, mState);
        getActivity().getSupportLoaderManager().initLoader(getLoaderId(), bundle, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSupportLoaderManager().destroyLoader(getLoaderId());
    }

    @Override
    public Loader<IssueListResponse> onCreateLoader(int id, Bundle args) {
        return new AsyncRepositoryIssuesLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<IssueListResponse> loader, IssueListResponse response) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        if (response != null && response.getIssues() != null)
            mIssuesAdapter.setResource(response.getIssues());
        else
            Snackbar.make(mViewGroupContainer, R.string.msg_repositoy_issues_loading_fail, Snackbar.LENGTH_SHORT).show();

        mIssuesAdapter.notifyDataSetChanged();

        // Hidden refresh bar
        mSwipeRefreshLayout.setRefreshing(false);

        mLastIssuesListResponse = response;
    }

    @Override
    public void onLoaderReset(Loader<IssueListResponse> loader) {

    }

    private int getLoaderId() {
        switch (mState) {
            case CLOSED:
                return LOADER_ISSUES_CLOSED;
            case OPENED:
                return LOADER_ISSUES_OPENED;
            default:
                break;
        }

        return LOADER_ISSUES_CLOSED;
    }
}
