package instinctools.android.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import instinctools.android.adapters.events.EventsAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.listeners.EndlessRecyclerOnScrollListener;
import instinctools.android.loaders.AsyncUserEventsLoader;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.events.EventsListResponse;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.events.GithubServiceEvents;

public class ProfileEventsFragment extends Fragment implements LoaderManager.LoaderCallbacks<EventsListResponse>, SwipeRefreshLayout.OnRefreshListener {
    // View
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private AbstractRecyclerAdapter mEventsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    EventsListResponse mLastEventsListResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_events, null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_profile_events);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_profile_events);
        mProgressBar.setVisibility(View.VISIBLE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_profile_events);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                if (mLastEventsListResponse == null || mLastEventsListResponse.getPageLinks().getNext() == null)
                    return;

                mEventsAdapter.getResource().add(null);
                mRecyclerView.post(new Runnable() {
                    public void run() {
                        mEventsAdapter.notifyItemInserted(mEventsAdapter.getResource().size() - 1);
                    }
                });
                GithubServiceEvents.getEventsByUrl(mLastEventsListResponse.getPageLinks().getNext(), new GithubServiceListener<EventsListResponse>() {
                    @Override
                    public void onError(int code, @Nullable ErrorResponse response) {
                        mEventsAdapter.getResource().remove(mEventsAdapter.getResource().size() - 1);
                        mEventsAdapter.notifyItemRemoved(mEventsAdapter.getResource().size() - 1);
                        mLoading = false;
                    }

                    @Override
                    public void onSuccess(EventsListResponse response) {
                        if (response != null) {
                            int saveOldPos = mEventsAdapter.getResource().size() - 1;
                            mEventsAdapter.getResource().remove(saveOldPos);
                            mEventsAdapter.getResource().addAll(response.getEvents());
                            mEventsAdapter.notifyItemRemoved(saveOldPos);
                            mEventsAdapter.notifyItemRangeInserted(saveOldPos + 1, response.getEvents().size());
                            mLastEventsListResponse = response;
                        }

                        mLoading = false;
                    }
                });
            }
        });

        mEventsAdapter = new EventsAdapter(getContext(), true);
        mRecyclerView.setAdapter(mEventsAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, false));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(ProfileActivity.LOADER_EVENTS_ID, Bundle.EMPTY, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSupportLoaderManager().destroyLoader(ProfileActivity.LOADER_EVENTS_ID);
    }

    @Override
    public Loader<EventsListResponse> onCreateLoader(int id, Bundle args) {
        return new AsyncUserEventsLoader(getContext(), ((ProfileActivity)getActivity()).getUserName());
    }

    @Override
    public void onLoadFinished(Loader<EventsListResponse> loader, EventsListResponse response) {
        if (response != null) {
            mEventsAdapter.setResource(response.getEvents());
            mEventsAdapter.notifyDataSetChanged();
        }

        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

        mLastEventsListResponse = response;
    }

    @Override
    public void onLoaderReset(Loader<EventsListResponse> loader) {

    }

    @Override
    public void onRefresh() {
        getActivity().getSupportLoaderManager().getLoader(ProfileActivity.LOADER_EVENTS_ID).forceLoad();
    }
}
