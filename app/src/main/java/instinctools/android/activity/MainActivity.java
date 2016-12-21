package instinctools.android.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.Book;
import instinctools.android.R;
import instinctools.android.adapters.BookAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.readers.FileReader;
import instinctools.android.readers.JsonBookReader;

public class MainActivity extends AppCompatActivity {
    private static final String BUNDLE_BOOKS = "BOOKS";

    private RecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;
    private List<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(BUNDLE_BOOKS))
                mBooks = savedInstanceState.getParcelableArrayList(BUNDLE_BOOKS);
            else
                readBooks();
        } else
            readBooks();

        initView();
    }

    private void readBooks() {
        String str = new FileReader(this).read(R.raw.data);
        mBooks = new JsonBookReader().read(str);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_book_list);

        mBookAdapter = new BookAdapter(this, mRecyclerView, mBooks);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mBookAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_item_child_layout_margin), ContextCompat.getDrawable(this, R.drawable.line_divider)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mBooks != null)
            outState.putParcelableArrayList(BUNDLE_BOOKS, (ArrayList<Book>)mBooks);
    }
}
