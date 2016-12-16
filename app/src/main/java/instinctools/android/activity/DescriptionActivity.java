package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.Book;
import instinctools.android.R;
import instinctools.android.adapters.BookAdapter;
import instinctools.android.utility.DrawableFactory;

public class DescriptionActivity extends AppCompatActivity {

    private ImageView mImageViewBook;
    private TextView mTextViewTitle;
    private TextView mTextViewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        
        initView();

        Intent intent = getIntent();
        if (intent != null) {
            Book book = intent.getParcelableExtra(BookAdapter.EXTRA_BOOK_TAG);

            mTextViewTitle.setText(book.getTitle());
            mTextViewDescription.setText(book.getDescription());

            mImageViewBook.setImageDrawable(DrawableFactory.createFromAssets(this, book.getImage()));
        }
    }

    private void initView() {
        mImageViewBook = (ImageView) findViewById(R.id.image_book);
        mTextViewTitle = (TextView) findViewById(R.id.text_title);
        mTextViewDescription = (TextView) findViewById(R.id.text_description);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
