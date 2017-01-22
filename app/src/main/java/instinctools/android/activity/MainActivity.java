package instinctools.android.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.adapters.RepositoryAdapter;
import instinctools.android.broadcasts.OnAlarmReceiver;
import instinctools.android.database.providers.RepositoriesOwnerProvider;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.imageloader.transformers.CircleImageTransformer;
import instinctools.android.loaders.AsyncUserInfoLoader;
import instinctools.android.models.github.user.User;
import instinctools.android.services.HttpUpdateMyRepositoriesService;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.authorization.GithubServiceAuthorization;
import instinctools.android.utility.Services;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainActivity";

    public static final int PERMISSION_EXTERNAL_STORAGE = 100;

    private static final int LOADER_REPOSITORIES_ID = 1;
    private static final int LOADER_USER_ID = 2;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RepositoryAdapter mRepositoryAdapter;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        requestExternalStoragePermissions();

        Intent intentService = new Intent(this, HttpUpdateMyRepositoriesService.class);
        startService(intentService);

        getSupportLoaderManager().initLoader(LOADER_REPOSITORIES_ID, null, this);
        getSupportLoaderManager().initLoader(LOADER_USER_ID, null, new LoaderManager.LoaderCallbacks<User>() {
            @Override
            public Loader<User> onCreateLoader(int id, Bundle args) {
                return new AsyncUserInfoLoader(MainActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<User> loader, User user) {
                if (user == null) {
                    Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return;
                }

                // Update navigate drawer
                updateNavBarInfo(user);
            }

            @Override
            public void onLoaderReset(Loader<User> loader) {

            }
        });
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_main_recycler);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_main_repositories_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_repository_list);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mRepositoryAdapter = new RepositoryAdapter(this, mRecyclerView, null);
        mRecyclerView.setAdapter(mRepositoryAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, true));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void updateNavBarInfo(User user) {
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
                startActivity(intent);
            }
        });

        textViewUsername.setText(user.getName());
        textViewEmail.setText(user.getEmail());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_EXTERNAL_STORAGE && grantResults.length == 2) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(findViewById(R.id.content_main), R.string.msg_permission_external_storage_granted, Snackbar.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_REPOSITORIES_ID)
            return null;

        return new CursorLoader(this, RepositoriesProvider.REPOSITORY_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }

        mRepositoryAdapter.changeCursor(cursor);

        // Hidden refresh bar
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void requestExternalStoragePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_EXTERNAL_STORAGE);
    }

    @Override
    public void onRefresh() {
        Intent intentService = new Intent(this, HttpUpdateMyRepositoriesService.class);
        startService(intentService);
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
                    public void onError(int code) {
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
            case R.id.nav_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        // Stop alarm manager
        Services.stopAlarmBroadcast(this, OnAlarmReceiver.class, OnAlarmReceiver.REQUEST_MY_REPO_CODE);
        Services.stopAlarmBroadcast(this, OnAlarmReceiver.class, OnAlarmReceiver.REQUEST_WATCH_REPO_CODE);
        Services.stopAlarmBroadcast(this, OnAlarmReceiver.class, OnAlarmReceiver.REQUEST_STARS_REPO_CODE);

        // Cleanup
        getContentResolver().delete(RepositoriesProvider.REPOSITORY_CONTENT_URI, null, null);
        getContentResolver().delete(RepositoriesOwnerProvider.REPOSITORY_OWNER_CONTENT_URI, null, null);

        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        finish();
    }
}
