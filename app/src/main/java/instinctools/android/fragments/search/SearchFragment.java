package instinctools.android.fragments.search;

import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.HashMap;
import java.util.Map;

import instinctools.android.R;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.adapters.search.SearchCommitsAdapter;
import instinctools.android.adapters.search.SearchIssuesAdapter;
import instinctools.android.adapters.search.SearchRepositoriesAdapter;
import instinctools.android.adapters.search.SearchUsersAdapter;
import instinctools.android.database.providers.SearchSuggestionsProvider;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.fragments.search.enums.SearchFragmentType;
import instinctools.android.listeners.EndlessRecyclerOnScrollListener;
import instinctools.android.loaders.search.AsyncSearchRequestLoader;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.search.CommitsSearchRequest;
import instinctools.android.models.github.search.IssuesSearchRequest;
import instinctools.android.models.github.search.RepositoriesSearchRequest;
import instinctools.android.models.github.search.SearchRequest;
import instinctools.android.models.github.search.SearchResponse;
import instinctools.android.models.github.search.UsersSearchRequest;
import instinctools.android.models.github.search.enums.SearchOrderType;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.search.GithubServiceSearch;
import instinctools.android.сustomViews.CustomSlidingDrawer;
import instinctools.android.сustomViews.listeners.CustomSlidingDrawerStateListener;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<SearchResponse> {
    private static final int LOADER_SEARCH_REQUEST_REPOSITORIES_ID = 1;
    private static final int LOADER_SEARCH_REQUEST_COMMITS_ID = 2;
    private static final int LOADER_SEARCH_REQUEST_ISSUES_ID = 3;
    private static final int LOADER_SEARCH_REQUEST_USERS_ID = 4;

    public static final String ARGUMENT_PAGE_TYPE = "PAGE_TYPE";
    public static final String BUNDLE_SEARCH_REQUEST = "SEARCH_REQUEST";

    private SearchFragmentType mType;

    // View
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private AbstractRecyclerAdapter mSearchAdapter;
    private ViewGroup mFilterContainer;
    private CustomSlidingDrawer mCustomVerticalSlidingDrawer;

    private SearchRequest mLastSearchRequest;
    private SearchResponse mLastSearchResponse;

    public SearchFragment() {
        this.mType = SearchFragmentType.REPOSITORIES;
    }

    public static SearchFragment newInstance(SearchFragmentType type) {
        SearchFragment fragment = new SearchFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_TYPE, type.ordinal());
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            mType = SearchFragmentType.values()[getArguments().getInt(ARGUMENT_PAGE_TYPE)];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_search_result_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mSearchAdapter = createSearchAdapter();
        mRecyclerView.setAdapter(mSearchAdapter);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                if (mLastSearchResponse == null || mLastSearchResponse.getPageLinks().getNext() == null)
                    return;

                mSearchAdapter.getResource().add(null);
                mRecyclerView.post(new Runnable() {
                    public void run() {
                        mSearchAdapter.notifyItemInserted(mSearchAdapter.getResource().size() - 1);
                    }
                });
                GithubServiceSearch.getSearchByUrl(mLastSearchResponse.getPageLinks().getNext(), new GithubServiceListener<SearchResponse>() {
                    @Override
                    public void onError(int code, @Nullable ErrorResponse response) {
                        mSearchAdapter.getResource().remove(mSearchAdapter.getResource().size() - 1);
                        mSearchAdapter.notifyItemRemoved(mSearchAdapter.getResource().size() - 1);
                        mLoading = false;
                    }

                    @Override
                    public void onSuccess(SearchResponse response) {
                        if (response != null) {
                            int saveOldPos = mSearchAdapter.getResource().size() - 1;
                            mSearchAdapter.getResource().remove(saveOldPos);
                            mSearchAdapter.getResource().addAll(response.getResponse());
                            mSearchAdapter.notifyItemRemoved(saveOldPos);
                            mSearchAdapter.notifyItemRangeInserted(saveOldPos + 1, response.getResponse().size());
                            mLastSearchResponse = response;
                        }

                        mLoading = false;
                    }
                });
            }
        });

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, false));

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_search_loading);

        mCustomVerticalSlidingDrawer = (CustomSlidingDrawer) view.findViewById(R.id.slider_search_filter);
        mCustomVerticalSlidingDrawer.setOnStateListener(new CustomSlidingDrawerStateListener() {
            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
                if (mLastSearchRequest != null) {
                    mLastSearchRequest.setFilters(getFilters());

                    mLastSearchRequest.setSort(getSortType());
                    mLastSearchRequest.setOrder(getOrderType());
                }

                search(mLastSearchRequest);
            }
        });

        mFilterContainer = (ViewGroup) view.findViewById(R.id.layout_search_filter_container);

        inflater.inflate(getFilterViewId(), mFilterContainer);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSupportLoaderManager().destroyLoader(getLoaderId());
    }

    public SearchFragmentType getFragmentType() {
        return mType;
    }

    public void search(SearchRequest request) {
        if (request == null)
            return;

        if (TextUtils.isEmpty(request.getText()))
            return;

        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mLastSearchRequest = request;

        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_SEARCH_REQUEST, request);

        getLoaderManager().restartLoader(getLoaderId(), bundle, this);
    }

    @Override
    public Loader<SearchResponse> onCreateLoader(int id, Bundle args) {
        return new AsyncSearchRequestLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<SearchResponse> loader, SearchResponse response) {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        mLastSearchResponse = response;

        mSearchAdapter.setResource(response.getResponse());
        mSearchAdapter.notifyDataSetChanged();

        if (response.getTotalCount() > 0) {
            SearchRecentSuggestions searchSuggestions = new SearchRecentSuggestions(getContext(), SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
            searchSuggestions.saveRecentQuery(mLastSearchRequest.getText(), null);
        }
    }

    @Override
    public void onLoaderReset(Loader<SearchResponse> loader) {

    }

    private int getLoaderId() {
        switch (mType) {
            case REPOSITORIES:
                return LOADER_SEARCH_REQUEST_REPOSITORIES_ID;
            case COMMITS:
                return LOADER_SEARCH_REQUEST_COMMITS_ID;
            case ISSUES:
                return LOADER_SEARCH_REQUEST_ISSUES_ID;
            case USERS:
                return LOADER_SEARCH_REQUEST_USERS_ID;
            default:
                throw new UnsupportedOperationException("Unsupported loader id by type: " + mType);
        }
    }

    private AbstractRecyclerAdapter createSearchAdapter() {
        switch (mType) {
            case REPOSITORIES:
                return new SearchRepositoriesAdapter(getContext());
            case COMMITS:
                return new SearchCommitsAdapter(getContext());
            case ISSUES:
                return new SearchIssuesAdapter(getContext());
            case USERS:
                return new SearchUsersAdapter(getContext());
            default:
                throw new UnsupportedOperationException("Unsupported search fragment type: " + mType);
        }
    }

    private int getFilterViewId() {
        int resViewId;
        switch (mType) {
            case REPOSITORIES:
                resViewId = R.layout.fragment_search_repository_filter;
                break;
            case COMMITS:
                resViewId = R.layout.fragment_search_commit_filter;
                break;
            case ISSUES:
                resViewId = R.layout.fragment_search_issue_filter;
                break;
            case USERS:
                resViewId = R.layout.fragment_search_user_filter;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported fragment type for get filter view " + mType);
        }

        return resViewId;
    }

    public String getSortType() {
        Spinner sortSpinner = (Spinner)getActivity().findViewById(R.id.spinner_search_sort);
        return sortSpinner.getSelectedItem().toString().toLowerCase();
    }

    public SearchOrderType getOrderType() {
        Spinner orderSpinner = (Spinner)getActivity().findViewById(R.id.spinner_search_order);
        return SearchOrderType.get(orderSpinner.getSelectedItem().toString().toLowerCase());
    }

    public Map<String, String> getFilters() {
        Map<String, String> filters = new HashMap<>();
        switch (mType) {
            case REPOSITORIES: {
                EditText forks = (EditText)getActivity().findViewById(R.id.text_repo_filter_forks);
                if (!TextUtils.isEmpty(forks.getText()))
                    filters.put(RepositoriesSearchRequest.FILTER_FORKS, forks.getText().toString());

                EditText size = (EditText)getActivity().findViewById(R.id.text_repo_filter_size);
                if (!TextUtils.isEmpty(size.getText()))
                    filters.put(RepositoriesSearchRequest.FILTER_SIZE, size.getText().toString());

                EditText stars = (EditText)getActivity().findViewById(R.id.text_repo_filter_stars);
                if (!TextUtils.isEmpty(stars.getText()))
                    filters.put(RepositoriesSearchRequest.FILTER_STARS, stars.getText().toString());

                Switch fork = (Switch)getActivity().findViewById(R.id.switch_repo_filter_fork);
                filters.put(RepositoriesSearchRequest.FILTER_FORK, String.valueOf(fork.isChecked()));

                Spinner isSpinner = (Spinner)getActivity().findViewById(R.id.spinner_repo_filter_is);
                filters.put(RepositoriesSearchRequest.FILTER_IS, isSpinner.getSelectedItem().toString().toLowerCase());
                break;
            }
            case COMMITS: {
                EditText author = (EditText)getActivity().findViewById(R.id.text_commit_filter_author);
                if (!TextUtils.isEmpty(author.getText()))
                    filters.put(CommitsSearchRequest.FILTER_AUTHOR, author.getText().toString());

                EditText committer = (EditText)getActivity().findViewById(R.id.text_commit_filter_committer);
                if (!TextUtils.isEmpty(committer.getText()))
                    filters.put(CommitsSearchRequest.FILTER_COMMITTER, committer.getText().toString());

                Switch merge = (Switch)getActivity().findViewById(R.id.switch_commit_filter_merge);
                filters.put(CommitsSearchRequest.FILTER_MERGE, String.valueOf(merge.isChecked()));
                break;
            }
            case ISSUES: {
                EditText assignee = (EditText)getActivity().findViewById(R.id.text_issue_filter_assignee);
                if (!TextUtils.isEmpty(assignee.getText()))
                    filters.put(IssuesSearchRequest.FILTER_ASSIGNEE, assignee.getText().toString());

                EditText author = (EditText)getActivity().findViewById(R.id.text_issue_filter_author);
                if (!TextUtils.isEmpty(author.getText()))
                    filters.put(IssuesSearchRequest.FILTER_AUTHOR, author.getText().toString());

                EditText comments = (EditText)getActivity().findViewById(R.id.text_issue_filter_comments);
                if (!TextUtils.isEmpty(comments.getText()))
                    filters.put(IssuesSearchRequest.FILTER_COMMENTS, comments.getText().toString());

                Spinner type = (Spinner)getActivity().findViewById(R.id.spinner_issue_filter_type);
                String issueType = getResources().getStringArray(R.array.entries_issue_type_values)[type.getSelectedItemPosition()];
                filters.put(IssuesSearchRequest.FILTER_TYPE, issueType);

                Spinner state = (Spinner)getActivity().findViewById(R.id.spinner_issue_filter_state);
                String issueState = getResources().getStringArray(R.array.entries_issue_state_values)[state.getSelectedItemPosition()];
                filters.put(IssuesSearchRequest.FILTER_STATE, issueState);
                break;
            }
            case USERS: {
                EditText repos = (EditText)getActivity().findViewById(R.id.text_user_filter_repos);
                if (!TextUtils.isEmpty(repos.getText()))
                    filters.put(UsersSearchRequest.FILTER_REPOS, repos.getText().toString());

                EditText followers = (EditText)getActivity().findViewById(R.id.text_user_filter_followers);
                if (!TextUtils.isEmpty(followers.getText()))
                    filters.put(UsersSearchRequest.FILTER_FOLLOWERS, followers.getText().toString());

                EditText language = (EditText)getActivity().findViewById(R.id.text_user_filter_language);
                if (!TextUtils.isEmpty(language.getText()))
                    filters.put(UsersSearchRequest.FILTER_LANGUAGE, language.getText().toString().toLowerCase());

                EditText location = (EditText)getActivity().findViewById(R.id.text_user_filter_location);
                if (!TextUtils.isEmpty(location.getText()))
                    filters.put(UsersSearchRequest.FILTER_LOCATION, location.getText().toString());

                Spinner type = (Spinner)getActivity().findViewById(R.id.spinner_user_filter_type);
                String userType = getResources().getStringArray(R.array.entries_user_type_values)[type.getSelectedItemPosition()];
                filters.put(UsersSearchRequest.FILTER_TYPE, userType);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported fragment type: " + mType);
        }

        return filters;
    }
}
