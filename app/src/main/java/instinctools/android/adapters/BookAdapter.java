package instinctools.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.activity.DescriptionActivity;
import instinctools.android.data.Book;
import instinctools.android.imageloader.ImageLoader;

/**
 * Created by orion on 16.12.16.
 */

public class BookAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private RecyclerView mRecyclerView;

    public static final String EXTRA_BOOK_TAG = "BOOK";

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_HEADER = 2;
    private static final int VIEW_TYPE_EMPTY = 3;

    public BookAdapter(Context context, RecyclerView recyclerView, Cursor cursor) {
        super(context, cursor);
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    public void onClick(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
        Book book = null;//getItem(position);
        Intent intent = new Intent(mContext, DescriptionActivity.class);
        intent.putExtra(EXTRA_BOOK_TAG, book);
        mContext.startActivity(intent);
    }

    private class BookItemHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTitle;
        private TextView mDescription;

        BookItemHolder(View view) {
            super(view);

            mImageView = (ImageView) view.findViewById(R.id.image_book);
            mTitle = (TextView) view.findViewById(R.id.text_title);
            mDescription = (TextView) view.findViewById(R.id.text_description);
        }

        private void onBindViewHolder(Cursor cursor) {
            Book item = Book.fromCursor(cursor);

            ImageLoader.what(item.getImage()).
                    loading(R.drawable.ic_crop_original_orange_24dp).
                    error(R.drawable.ic_clear_red_24dp).
                    in(mImageView).
                    load();

            mTitle.setText(item.getTitle());
            mDescription.setText(item.getDescription());
        }
    }

    private class HeaderItemHolder extends RecyclerView.ViewHolder {
        HeaderItemHolder(View view) {
            super(view);
        }
    }

    private class EmptyItemHolder extends RecyclerView.ViewHolder {
        EmptyItemHolder(View view) {
            super(view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_header, parent, false);
                return new HeaderItemHolder(view);
            }
            case VIEW_TYPE_EMPTY: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_empty, parent, false);
                return new EmptyItemHolder(view);
            }
            case VIEW_TYPE_ITEM:
                break;
            default:
                throw new UnsupportedOperationException("Unsupported view item type: " + viewType);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_book, parent, false);
        view.setOnClickListener(this);
        return new BookItemHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataValid && mCursor != null) {
            if (position == 0)
                return mCursor.getCount() != 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_EMPTY;
        }

        return VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof BookItemHolder)
                ((BookItemHolder) holder).onBindViewHolder(cursor);
        }
    }
}