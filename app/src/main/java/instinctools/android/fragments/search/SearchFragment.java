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
import android.widget.ProgressBar;

import instinctools.android.R;
import instinctools.android.adapters.search.AbstractSearchAdapter;
import instinctools.android.adapters.search.SearchCommitsAdapter;
import instinctools.android.adapters.search.SearchIssuesAdapter;
import instinctools.android.adapters.search.SearchRepositoriesAdapter;
import instinctools.android.adapters.search.SearchUsersAdapter;
import instinctools.android.database.providers.SearchSuggestionsProvider;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.fragments.search.enums.SearchFragmentType;
import instinctools.android.listeners.OnLoadMoreListener;
import instinctools.android.loaders.AsyncSearchRequestLoader;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.search.SearchRequest;
import instinctools.android.models.github.search.SearchResponse;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.search.GithubServiceSearch;

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
    private AbstractSearchAdapter mSearchAdapter;

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
        mSearchAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mLastSearchResponse.getPageLinks().getNext() == null)
                    return;

                mSearchAdapter.getResource().add(null);
                /// TODO
                // Triggered
                // RecyclerView: Cannot call this method in a scroll callback. Scroll callbacks might be run during a measure & layout pass where you cannot change the RecyclerView data. Any method call that might change the structure of the RecyclerView or the adapter contents should be postponed to the next frame.
                mSearchAdapter.notifyItemInserted(mSearchAdapter.getResource().size() - 1);
                GithubServiceSearch.getSearchByUrl(mLastSearchResponse.getPageLinks().getNext(), new GithubServiceListener<SearchResponse>() {
                    @Override
                    public void onError(int code, @Nullable ErrorResponse response) {
                        mSearchAdapter.getResource().remove(mSearchAdapter.getResource().size() - 1);
                        mSearchAdapter.notifyItemRemoved(mSearchAdapter.getResource().size() - 1);
                        mSearchAdapter.setLoaded();
                    }

                    @Override
                    public void onSuccess(SearchResponse response) {
                        if (response != null) {
                            int saveOldPos = mSearchAdapter.getResource().size() - 1;
                            mSearchAdapter.getResource().remove(saveOldPos);
                            mSearchAdapter.getResource().addAll(mSearchAdapter.getResource());
                            mSearchAdapter.notifyItemRemoved(saveOldPos);
                            mSearchAdapter.notifyItemRangeInserted(saveOldPos + 1, response.getResponse().size());
                        }
                        mSearchAdapter.setLoaded();
                    }
                });
            }
        });
        mRecyclerView.setAdapter(mSearchAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, false));

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_search_loading);

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

    private AbstractSearchAdapter createSearchAdapter() {
        switch (mType) {
            case REPOSITORIES:
                return new SearchRepositoriesAdapter(getContext(), mRecyclerView);
            case COMMITS:
                return new SearchCommitsAdapter(getContext(), mRecyclerView);
            case ISSUES:
                return new SearchIssuesAdapter(getContext(), mRecyclerView);
            case USERS:
                return new SearchUsersAdapter(getContext(), mRecyclerView);
            default:
                throw new UnsupportedOperationException("Unsupported search fragment type: " + mType);
        }
    }
}
