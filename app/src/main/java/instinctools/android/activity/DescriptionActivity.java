package instinctools.android.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.adapters.RepositoryAdapter;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.RepositoriesProvider;
import instinctools.android.misc.LinkTransformationMethod;

public class DescriptionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ImageView mImageViewBook;
    private TextView mTextViewTitle;
    private TextView mTextViewDescription;
    private ProgressBar mProgressBar;

    private static final String BUNDLE_BOOK_ID = "ID";

    private static final int LOADER_BOOK_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        Intent intent = getIntent();
        if (intent != null) {
            long id = intent.getLongExtra(RepositoryAdapter.EXTRA_REPOSITORY_ID_TAG, -1);

            Bundle bundle = new Bundle();
            bundle.putLong(BUNDLE_BOOK_ID, id);
            getSupportLoaderManager().initLoader(LOADER_BOOK_ID, bundle, this);
        }
    }

    private void initView() {
        mImageViewBook = (ImageView) findViewById(R.id.image_book);
        mTextViewTitle = (TextView) findViewById(R.id.text_name);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_description_content);

        mTextViewDescription = (TextView) findViewById(R.id.text_description);
        mTextViewDescription.setTransformationMethod(new LinkTransformationMethod());
        mTextViewDescription.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_BOOK_ID)
            return null;

        return new CursorLoader(this, RepositoriesProvider.REPOSITORY_CONTENT_URI, null, DBConstants.REPOSITORY_ID + " = ?", new String[]{String.valueOf(args.getLong(BUNDLE_BOOK_ID))}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mProgressBar.setVisibility(View.GONE);

        if (!cursor.moveToFirst())
            return;

        /*Book book = Book.fromCursor(cursor);

        mTextViewTitle.setText(book.getTitle());
        getSupportActionBar().setTitle(book.getTitle());
        mTextViewDescription.setText(book.getDescription());

        ImageLoader.what(book.getImage()).
                loading(R.drawable.ic_crop_original_orange_24dp).
                error(R.drawable.ic_clear_red_24dp).
                in(mImageViewBook).
                load();

        if (!cursor.isClosed())
            cursor.close();*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
