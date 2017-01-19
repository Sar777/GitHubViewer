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
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.adapters.RepositoryAdapter;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.http.HttpClientFactory;
import instinctools.android.http.OnHttpClientListener;
import instinctools.android.misc.LinkTransformationMethod;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.repositories.RepositoryReadme;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.repository.GithubServiceRepository;

public class DescriptionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ProgressBar mProgressBar;
    private TextView mTextViewReadme;

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

        mProgressBar = (ProgressBar) findViewById(R.id.pb_description_content);

        mTextViewReadme = (TextView) findViewById(R.id.text_readme);
        mTextViewReadme.setTransformationMethod(new LinkTransformationMethod());
        mTextViewReadme.setMovementMethod(LinkMovementMethod.getInstance());
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

        if (!cursor.moveToFirst())
            return;

        Repository repository = Repository.fromCursor(cursor);
        mCollapsingToolbarLayout.setTitle(repository.getName());

        GithubServiceRepository.getRepositoryReadme(repository.getRepositoryOwner().getLogin(), repository.getName(), new GithubServiceListener<RepositoryReadme>() {
            @Override
            public void onError(int code) {
            }

            @Override
            public void onSuccess(RepositoryReadme data) {
                new HttpClientFactory.HttpClient(data.getDownloadUrl()).send(new OnHttpClientListener() {
                    @Override
                    public void onError(int errCode) {

                    }

                    @Override
                    public void onSuccess(int code, String content) {
                        mTextViewReadme.setText(content);
                    }
                });
            }
        });

        if (!cursor.isClosed())
            cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
