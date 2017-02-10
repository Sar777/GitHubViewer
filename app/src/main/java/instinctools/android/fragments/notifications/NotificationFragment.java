package instinctools.android.fragments.notifications;

import android.accounts.Account;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.adapters.NotificationAdapter;
import instinctools.android.adapters.SimpleItemTouchHelperCallback;
import instinctools.android.constans.Constants;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.NotificationsProvider;
import instinctools.android.decorations.DividerItemDecoration;

public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_NOTIFICATION_UNREAD_ID = 1;
    private static final int LOADER_NOTIFICATION_PARTICIPATING_ID = 2;
    private static final int LOADER_NOTIFICATION_ALL_ID = 3;

    public static final String ARGUMENT_PAGE_TYPE = "PAGE_TYPE";

    private NotificationFragmentType mType;

    // View
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private NotificationAdapter mNotificationAdapter;

    public NotificationFragment() {
        this.mType = NotificationFragmentType.UNREAD;
    }

    public static NotificationFragment newInstance(NotificationFragmentType type) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_TYPE, type.ordinal());
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            mType = NotificationFragmentType.values()[getArguments().getInt(ARGUMENT_PAGE_TYPE)];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_notification_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mNotificationAdapter = new NotificationAdapter(getContext(), false, null);

        if (isCanSwipeItem()) {
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mNotificationAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerView);
        }

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, false));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_notification_recycler);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_notification_loading);

        return view;
    }

    @Override
    public void onRefresh() {
        Account account = App.getApplicationAccount();
        if (account == null) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        ContentResolver.requestSync(account, NotificationsProvider.AUTHORITY, Bundle.EMPTY);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setAdapter(mNotificationAdapter);
        getActivity().getSupportLoaderManager().initLoader(getLoaderId(), null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSupportLoaderManager().destroyLoader(getLoaderId());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), NotificationsProvider.NOTIFICATIONS_CONTENT_URI, null, DBConstants.NOTIFICATION_TYPE + " = ?", new String[] {String.valueOf(getDBNotificationType())}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        mNotificationAdapter.changeCursor(cursor, true);

        // Hidden refresh bar
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private int getLoaderId() {
        switch (mType) {
            case UNREAD:
                return LOADER_NOTIFICATION_UNREAD_ID;
            case PARTICIPATING:
                return LOADER_NOTIFICATION_PARTICIPATING_ID;
            case ALL:
                return LOADER_NOTIFICATION_ALL_ID;
            default:
                break;
        }

        return LOADER_NOTIFICATION_UNREAD_ID;
    }

    private int getDBNotificationType() {
        switch (mType) {
            case UNREAD:
                return Constants.NOTIFICATION_TYPE_UNREAD;
            case PARTICIPATING:
                return Constants.NOTIFICATION_TYPE_PARTICIPATING;
            case ALL:
                return Constants.NOTIFICATION_TYPE_ALL;
            default:
                break;
        }

        return Constants.NOTIFICATION_TYPE_ALL;
    }

    private boolean isCanSwipeItem() {
        return mType == NotificationFragmentType.UNREAD;
    }
}
