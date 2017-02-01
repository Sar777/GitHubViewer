package instinctools.android.activity;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import instinctools.android.R;
import instinctools.android.adapters.RepositoryAdapter;
import instinctools.android.constans.Constants;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.database.providers.SearchSuggestionsProvider;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.сustomViews.MaterialDialog;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener, SearchView.OnSuggestionListener, MenuItem.OnMenuItemClickListener {
    private static final int LOADER_REPOSITORIES_ID = 1;
    private static final int QUERY_SEARCH_DELAY = 300;

    public static final String INTENT_SEARCH_REQUEST = "SEARCH_REQUEST";

    // View
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SearchView mSearchView;

    private PopupMenu mPopupMenu;

    // Models
    private RepositoryAdapter mRepositoryAdapter;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mTextQueryWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();

        getSupportLoaderManager().initLoader(LOADER_REPOSITORIES_ID, null, this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_search_result);
        mProgressBar.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_search_repository);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mRepositoryAdapter = new RepositoryAdapter(this, mRecyclerView, false, null);
        mRecyclerView.setAdapter(mRepositoryAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, false));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_search_menu, menu);

        MenuItem menuSearch = menu.findItem(R.id.action_search);

        MenuItem filterItem = menu.findItem(R.id.action_filter);
        filterItem.setOnMenuItemClickListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (menuSearch != null)
            mSearchView = (SearchView) menuSearch.getActionView();

        if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setOnSuggestionListener(this);
            mSearchView.setFocusable(true);
            mSearchView.setIconified(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_REPOSITORIES_ID)
            return null;

        return new CursorLoader(this,
                RepositoriesProvider.REPOSITORY_CONTENT_URI,
                null, DBConstants.TABLE_REPOSITORIES + "." + DBConstants.REPOSITORY_TYPE + " = ?",
                new String[]{String.valueOf(Constants.REPOSITORY_TYPE_SEARCH)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mRepositoryAdapter.changeCursor(cursor, true);

        if (cursor.getCount() > 0 && !TextUtils.isEmpty(mSearchView.getQuery())) {
            SearchRecentSuggestions searchSuggestions = new SearchRecentSuggestions(SearchActivity.this, SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
            searchSuggestions.saveRecentQuery(mSearchView.getQuery().toString(), null);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        search();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void search() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        /*SearchRequest request = new SearchRequest(mSearchView.getQuery().toString());
        request.setSort(mSpinnerSort.getSelectedItem().toString());

        Intent intent = new Intent(this, HttpSearchRepositoryService.class);
        intent.putExtra(INTENT_SEARCH_REQUEST, request);
        startService(intent);*/
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mHandler.removeCallbacks(mTextQueryWorker);
        mTextQueryWorker = new Runnable() {
            @Override
            public void run() {
                search();
            }
        };
        mHandler.postDelayed(mTextQueryWorker, QUERY_SEARCH_DELAY);

        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        String suggestion = getSuggestion(position);
        mSearchView.setQuery(suggestion, true);
        return true;
    }

    private String getSuggestion(int position) {
        Cursor cursor = (Cursor) mSearchView.getSuggestionsAdapter().getItem(position);
        String suggest = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
        return suggest;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.setTitle(getString(R.string.title_search_settings_dialog))
                .setCustomViewResource(R.layout.dialog_search_settings)
                .setupPositiveButton("Применить", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }

                });
        dialog.show();

        return true;
    }
}
