package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.adapters.BookAdapter;
import instinctools.android.data.Book;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.misc.LinkTransformationMethod;

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
        
        initView();

        Intent intent = getIntent();
        if (intent != null) {
            Book book = intent.getParcelableExtra(BookAdapter.EXTRA_BOOK_TAG);

            mTextViewTitle.setText(book.getTitle());
            getSupportActionBar().setTitle(book.getTitle());
            mTextViewDescription.setText(book.getDescription());

            ImageLoader.with(this).
                    what(book.getImage()).
                    loading(R.drawable.ic_crop_original_orange_24dp).
                    error(R.drawable.ic_clear_red_24dp).
                    in(mImageViewBook).
                    load();
        }
    }

    private void initView() {
        mImageViewBook = (ImageView) findViewById(R.id.image_book);
        mTextViewTitle = (TextView) findViewById(R.id.text_title);

        mTextViewDescription = (TextView) findViewById(R.id.text_description);
        mTextViewDescription.setTransformationMethod(new LinkTransformationMethod());
        mTextViewDescription.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
