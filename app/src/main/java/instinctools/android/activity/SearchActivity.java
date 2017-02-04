package instinctools.android.activity;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import instinctools.android.R;
import instinctools.android.adapters.search.SearchTypeAdapter;
import instinctools.android.fragments.search.SearchFragment;
import instinctools.android.fragments.search.enums.SearchFragmentType;
import instinctools.android.models.github.search.CommitsSearchRequest;
import instinctools.android.models.github.search.IssuesSearchRequest;
import instinctools.android.models.github.search.RepositoriesSearchRequest;
import instinctools.android.models.github.search.SearchRequest;
import instinctools.android.models.github.search.UsersSearchRequest;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener, MenuItem.OnMenuItemClickListener, ViewPager.OnPageChangeListener {
    private static final int QUERY_SEARCH_DELAY = 300;

    // View
    private SearchView mSearchView;
    private ViewPager mViewPager;
    private SearchTypeAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mTextQueryWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_search);

        mViewPager = (ViewPager) findViewById(R.id.pager_search);
        mPagerAdapter = new SearchTypeAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(SearchTypeAdapter.NUM_PAGES);

        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); ++i)
            mTabLayout.getTabAt(i).setIcon(getTabLayoutDrawable(i));

        mSearchView = (SearchView) findViewById(R.id.searchview_search);
        mSearchView.setIconified(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSuggestionListener(this);
        mSearchView.onActionViewExpanded();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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

        MenuItem menuOrder = menu.findItem(R.id.action_order);
        MenuItem filterItem = menu.findItem(R.id.action_sort);

        menuOrder.setOnMenuItemClickListener(this);
        filterItem.setOnMenuItemClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        mHandler.removeCallbacks(mTextQueryWorker);
        mTextQueryWorker = new Runnable() {
            @Override
            public void run() {
                searchInTab(mViewPager.getCurrentItem());
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
        return cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return true;
    }

    private int getTabLayoutDrawable(int index) {
        switch (SearchFragmentType.get(index)) {
            case REPOSITORIES:
                return R.drawable.ic_github_repo_white;
            case COMMITS:
                return R.drawable.ic_github_commit_white;
            case ISSUES:
                return R.drawable.ic_github_issue_white;
            case USERS:
                return R.drawable.ic_github_person_white;
            default:
                throw new UnsupportedOperationException("Unsupported search enum type: " + index);
        }
    }

    private void searchInTab(int position) {
        SearchFragment fragment = (SearchFragment)(mPagerAdapter.getRegisteredFragment(position));
        SearchRequest request;
        switch (fragment.getFragmentType()) {
            case REPOSITORIES:
                request = new RepositoriesSearchRequest(mSearchView.getQuery().toString());
                break;
            case COMMITS:
                request = new CommitsSearchRequest(mSearchView.getQuery().toString());
                break;
            case ISSUES:
                request = new IssuesSearchRequest(mSearchView.getQuery().toString());
                break;
            case USERS:
                request =  new UsersSearchRequest(mSearchView.getQuery().toString());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported fragment type for send search request: " + fragment.getFragmentType());
        }

        fragment.search(request);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        searchInTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
