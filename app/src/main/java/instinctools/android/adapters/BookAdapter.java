package instinctools.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import instinctools.android.Book;
import instinctools.android.R;

/**
 * Created by orion on 16.12.16.
 */

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Book> mResources;

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_HEADER = 2;

    public BookAdapter(List<Book> resources) {
        mResources = resources;
    }

    public void add(int position, Book item) {
        mResources.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mResources.indexOf(item);
        mResources.remove(position);
        notifyItemRemoved(position);
    }

    public class BookItemHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTitle;
        private TextView mDescription;

        public BookItemHolder(View view) {
            super(view);

            mImageView = (ImageView) view.findViewById(R.id.image_book);
            mTitle = (TextView) view.findViewById(R.id.text_title);
            mDescription = (TextView) view.findViewById(R.id.text_description);
        }

        private void onBindViewHolder(int position) {
            Book item = mResources.get(position);

            mTitle.setText(item.getTitle());
            mDescription.setText(item.getDescription().length() > 50 ? item.getDescription().substring(0, 50) + "..." : item.getDescription());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof BookItemHolder)
                ((BookItemHolder)holder).onBindViewHolder(position);
        } else {
            // TODO header
        }

    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM; // TODO impl header type
    }

    @Override
    public int getItemCount() {
        return mResources.size();
    }

}