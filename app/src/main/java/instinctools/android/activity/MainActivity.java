package instinctools.android.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import instinctools.android.Book;
import instinctools.android.R;
import instinctools.android.adapters.BookAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.readers.FileReader;
import instinctools.android.readers.JsonBookReader;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_book_list);

        String str = new FileReader(this).read(R.raw.data);
        List<Book> objects = new JsonBookReader().read(str);
        mBookAdapter = new BookAdapter(this, mRecyclerView, objects);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mBookAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable.line_divider)));
    }
}
