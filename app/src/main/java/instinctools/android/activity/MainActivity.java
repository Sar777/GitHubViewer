package instinctools.android.activity;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.R;
import instinctools.android.adapters.BookAdapter;
import instinctools.android.constans.Constants;
import instinctools.android.data.Book;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.loaders.AsyncHttpLoader;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private static final String TAG = "MainActivity";

    private static final String BUNDLE_BOOKS = "BOOKS";

    private static final int LOADER_CONTENT_ID = 1;

    public static final String BUNDLE_LOADER_URL = "LOADER_URL";

    private RecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;
    private List<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        if (savedInstanceState == null || !savedInstanceState.containsKey(BUNDLE_BOOKS)) {
            Bundle bundle = new Bundle();
            bundle.putString(BUNDLE_LOADER_URL, Constants.API_URL);
            getSupportLoaderManager().initLoader(LOADER_CONTENT_ID, bundle, this);
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_book_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_item_child_layout_margin), ContextCompat.getDrawable(this, R.drawable.line_divider)));

        mBookAdapter = new BookAdapter(this, mRecyclerView, mBooks);
        mRecyclerView.setAdapter(mBookAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mBooks != null)
            outState.putParcelableArrayList(BUNDLE_BOOKS, (ArrayList<Book>) mBooks);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(BUNDLE_BOOKS)) {
            mBooks = savedInstanceState.getParcelableArrayList(BUNDLE_BOOKS);
            mBookAdapter.setResources(mBooks);
            mBookAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Loader<List<Book>> loader = null;
        if (id == LOADER_CONTENT_ID)
            loader = new AsyncHttpLoader(this, args);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        mBooks = data;
        mBookAdapter.setResources(mBooks);
        mBookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
    }
}
