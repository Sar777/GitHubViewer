package instinctools.android.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.adapters.RepositoryAdapter;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.models.github.repositories.Repository;

public class DescriptionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // View
    private ViewGroup mLayoutCardView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ProgressBar mProgressBar;
    private TextView mTextViewFullName;
    private TextView mTextViewDescription;
    private TextView mTextViewLanguage;
    private TextView mTextViewDefaultBranch;
    private TextView mTextViewForks;
    private TextView mTextViewStargazers;
    private TextView mTextViewWatchers;
    private TextView mTextViewOpenIssues;

    private static final String BUNDLE_REPOSITORY_ID = "ID";

    private static final int LOADER_REPOSITORY_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        initView();

        Intent intent = getIntent();
        if (intent != null) {
            long id = intent.getLongExtra(RepositoryAdapter.EXTRA_REPOSITORY_ID_TAG, -1);

            Bundle bundle = new Bundle();
            bundle.putLong(BUNDLE_REPOSITORY_ID, id);
            getSupportLoaderManager().initLoader(LOADER_REPOSITORY_ID, bundle, this);
        }
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitle("");

        mLayoutCardView = (ViewGroup) findViewById(R.id.layout_description_cards);
        mLayoutCardView.setVisibility(View.INVISIBLE);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_description_content);

        mTextViewFullName = (TextView) findViewById(R.id.text_description_fullname);
        mTextViewDescription = (TextView) findViewById(R.id.text_description_description);
        mTextViewLanguage = (TextView) findViewById(R.id.text_description_language);
        mTextViewDefaultBranch = (TextView) findViewById(R.id.text_description_default_branch);
        //
        mTextViewForks = (TextView) findViewById(R.id.text_description_forks);
        mTextViewStargazers = (TextView) findViewById(R.id.text_description_stargazers);
        mTextViewWatchers = (TextView) findViewById(R.id.text_description_watchers);
        mTextViewOpenIssues = (TextView) findViewById(R.id.text_description_open_issues);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_REPOSITORY_ID)
            return null;

        return new CursorLoader(this, RepositoriesProvider.REPOSITORY_CONTENT_URI, null, DBConstants.TABLE_REPOSITORIES + "." + DBConstants.REPOSITORY_ID + " = ?", new String[]{String.valueOf(args.getLong(BUNDLE_REPOSITORY_ID))}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mProgressBar.setVisibility(View.GONE);
        mLayoutCardView.setVisibility(View.VISIBLE);

        if (!cursor.moveToFirst())
            return;

        Repository repository = Repository.fromCursor(cursor);
        mCollapsingToolbarLayout.setTitle(repository.getName());

        mTextViewFullName.setText(repository.getFullName());
        mTextViewDescription.setText(repository.getDescription());
        mTextViewLanguage.setText(repository.getLanguage());
        mTextViewDefaultBranch.setText(repository.getDefaultBranch());
        //
        mTextViewForks.setText(String.valueOf(repository.getForks()));
        mTextViewStargazers.setText(String.valueOf(repository.getStargazers()));
        mTextViewWatchers.setText(String.valueOf(repository.getWatchers()));
        mTextViewOpenIssues.setText(String.valueOf(repository.getOpenIssues()));

        if (!cursor.isClosed())
            cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
