package instinctools.android.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.account.GitHubAccount;
import instinctools.android.adapters.events.EventsAdapter;
import instinctools.android.constans.Constants;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.NotificationsProvider;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.database.providers.SearchSuggestionsProvider;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.imageloader.transformers.CircleImageTransformer;
import instinctools.android.listeners.EndlessRecyclerOnScrollListener;
import instinctools.android.loaders.AsyncEventsLoader;
import instinctools.android.loaders.AsyncUserInfoLoader;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.events.EventsListResponse;
import instinctools.android.models.github.user.User;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.authorization.GithubServiceAuthorization;
import instinctools.android.services.github.events.GithubServiceEvents;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, MenuItem.OnMenuItemClickListener {
    private static final String TAG = "MainActivity";

    public static final int PERMISSION_GET_ACCOUNTS = 100;

    private static final int LOADER_EVENTS_ID = 1;
    private static final int LOADER_USER_ID = 2;
    private static final int LOADER_NOTIFICATIONS_ID = 3;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EventsAdapter mEventsAdapter;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    // Models
    private EventsListResponse mLastSearchResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initLoaders();
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_main_recycler);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_main_events_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_events_list);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mEventsAdapter = new EventsAdapter(this);
        mRecyclerView.setAdapter(mEventsAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, false));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                if (mLastSearchResponse == null || mLastSearchResponse.getPageLinks().getNext() == null)
                    return;

                mEventsAdapter.getResource().add(null);
                mRecyclerView.post(new Runnable() {
                    public void run() {
                        mEventsAdapter.notifyItemInserted(mEventsAdapter.getResource().size() - 1);
                    }
                });
                GithubServiceEvents.getEventsByUrl(mLastSearchResponse.getPageLinks().getNext(), new GithubServiceListener<EventsListResponse>() {
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
                            mLastSearchResponse = response;
                        }

                        mLoading = false;
                    }
                });
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
    }

    private void initLoaders() {
        // Notifications
        getSupportLoaderManager().initLoader(LOADER_NOTIFICATIONS_ID, null, this);
        // User info
        getSupportLoaderManager().initLoader(LOADER_USER_ID, new Bundle(), new LoaderManager.LoaderCallbacks<User>() {
            @Override
            public Loader<User> onCreateLoader(int id, Bundle args) {
                return new AsyncUserInfoLoader(MainActivity.this, args);
            }

            @Override
            public void onLoadFinished(Loader<User> loader, User user) {
                if (user == null) {
                    showUserGetAlert();
                    return;
                }

                App.setLoggedUser(user);

                // Update navigate drawer
                updateUserInfoNavBar(user);
            }

            @Override
            public void onLoaderReset(Loader<User> loader) {

            }
        });

        // All events
        getSupportLoaderManager().initLoader(LOADER_EVENTS_ID, null, new LoaderManager.LoaderCallbacks<EventsListResponse>() {
            @Override
            public Loader<EventsListResponse> onCreateLoader(int id, Bundle args) {
                return new AsyncEventsLoader(getApplicationContext());
            }

            @Override
            public void onLoaderReset(Loader<EventsListResponse> loader) {

            }

            @Override
            public void onLoadFinished(Loader<EventsListResponse> loader, EventsListResponse response) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);

                mEventsAdapter.setResource(response.getEvents());
                mEventsAdapter.notifyDataSetChanged();

                mLastSearchResponse = response;

                // Hidden refresh bar
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showUserGetAlert() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.msg_fail_get_current_user_title)
                .setMessage(R.string.msg_fail_get_current_user_summary)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setIcon(R.drawable.ic_github_logo)
                .show();
    }

    private void updateUserInfoNavBar(User user) {
        final ImageView imageAvatar = (ImageView) mNavigationView.findViewById(R.id.image_user_avatar);
        final TextView textViewUsername = (TextView) mNavigationView.findViewById(R.id.text_username);
        final TextView textViewEmail = (TextView) mNavigationView.findViewById(R.id.text_email);

        ImageLoader
                .what(user.getAvatarUrl())
                .in(imageAvatar)
                .transformer(new CircleImageTransformer())
                .load();

        // Enable clickable
        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, 0, 0);
                startActivity(intent, options.toBundle());
            }
        });

        textViewUsername.setText(user.getName());
        textViewEmail.setText(user.getEmail());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_GET_ACCOUNTS && grantResults.length == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Snackbar.make(findViewById(R.id.content_main), R.string.msg_fail_grant_account_permissions, Snackbar.LENGTH_SHORT).show();
            else
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
                    deleteAccount();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> cursor = null;
        switch (id) {
            case LOADER_NOTIFICATIONS_ID:
                cursor = new CursorLoader(this, NotificationsProvider.NOTIFICATIONS_CONTENT_URI, null, DBConstants.NOTIFICATION_TYPE + " = ?", new String[]{String.valueOf(Constants.NOTIFICATION_TYPE_UNREAD)}, null);
                break;
            default:
                break;
        }

        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_NOTIFICATIONS_ID: {
                ViewGroup view = (ViewGroup) mNavigationView.getMenu().findItem(R.id.nav_notification).getActionView();
                TextView textView = (TextView) view.getChildAt(0);
                textView.setVisibility(View.VISIBLE);
                if (cursor.getCount() >= Constants.MAX_GITHUB_NOTIFICATIONS)
                    textView.setText(cursor.getCount() + "+");
                else if (cursor.getCount() > 0)
                    textView.setText(String.valueOf(cursor.getCount()));
                else
                    textView.setVisibility(View.INVISIBLE);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported loader by id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().getLoader(LOADER_EVENTS_ID).forceLoad();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout: {
                GithubServiceAuthorization.logout(new GithubServiceListener<Boolean>() {
                    @Override
                    public void onError(int code, ErrorResponse response) {
                        Snackbar.make(findViewById(R.id.content_main), R.string.msg_sign_out_unknown_error, Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(Boolean data) {
                        logout();
                    }
                });
                break;
            }
            case R.id.nav_my_repositories:
                startActivity(new Intent(this, MyRepositoriesActivity.class));
                break;
            case R.id.nav_my_watch_repos:
                startActivity(new Intent(this, WatchRepositoriesActivity.class));
                break;
            case R.id.nav_my_star_repos:
                startActivity(new Intent(this, StarRepositoriesActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("NewApi")
    private void logout() {
        // Cleanup
        getContentResolver().delete(RepositoriesProvider.REPOSITORY_CONTENT_URI, null, null);
        getContentResolver().delete(NotificationsProvider.NOTIFICATIONS_CONTENT_URI, null, null);

        SearchRecentSuggestions searchSuggestions = new SearchRecentSuggestions(this, SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
        searchSuggestions.clearHistory();

        deleteAccount();

        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_base_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            default:
                break;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void deleteAccount() {
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.GET_ACCOUNTS }, PERMISSION_GET_ACCOUNTS);
        else {
            for (Account account : accountManager.getAccountsByType(GitHubAccount.TYPE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
                    accountManager.removeAccountExplicitly(account);
                else
                    accountManager.removeAccount(account, this, new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                        }
                    }, null);
            }
        }
    }
}
