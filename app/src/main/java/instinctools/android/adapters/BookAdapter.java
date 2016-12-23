package instinctools.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import instinctools.android.data.Book;
import instinctools.android.ImageLoader;
import instinctools.android.R;
import instinctools.android.activity.DescriptionActivity;

/**
 * Created by orion on 16.12.16.
 */

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<Book> mResources;
    private RecyclerView mRecyclerView;

    public static final String EXTRA_BOOK_TAG = "BOOK";

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_HEADER = 2;
    private static final int VIEW_TYPE_EMPTY = 3;

    public BookAdapter(Context context, RecyclerView recyclerView, List<Book> resources) {
        mContext = context;
        mRecyclerView = recyclerView;
        mResources = resources;
    }

    private Book getItem(int position) {
        return mResources.get(position - 1);
    }

    @Override
    public void onClick(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
        Book book = getItem(position);
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

        private void onBindViewHolder(int position) {
            Book item = getItem(position);

            //mImageView.setImageDrawable(DrawableFactory.createFromAssets(mContext, item.getImage()));

            ImageLoader loader = new ImageLoader.Builder(mContext, mImageView).error(R.drawable.ic_clear_red_24dp).placeholder(R.drawable.ic_crop_original_orange_24dp).build();
            loader.load(item.getImage());

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof BookItemHolder)
                ((BookItemHolder) holder).onBindViewHolder(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return !mResources.isEmpty() ? VIEW_TYPE_HEADER : VIEW_TYPE_EMPTY;

        return VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mResources != null ? mResources.size() + 1 : 0;
    }

    public void setResources(List<Book> books) {
        mResources = books;
    }
}