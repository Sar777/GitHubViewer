package instinctools.android.adapters.contents;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.fragments.repository.RepositoryContentFragment;

public class ContentPathAdapter extends AbstractRecyclerAdapter<String> {
    public ContentPathAdapter(@NonNull Context context) {
        super(context);
        getResource().add("/");
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        ItemViewHolder(View view) {
            super(view);

            mTextView = (TextView) view.findViewById(R.id.text_content_path_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getResource().size() - 1 == getAdapterPosition())
                        return;

                    String item = getItem(getAdapterPosition());
                    getResource().subList(getAdapterPosition(), getResource().size()).clear();
                    notifyDataSetChanged();

                    StringBuilder builder = new StringBuilder();
                    for (String path : getResource())
                        builder.append(path).append('/');

                    builder.append(item);

                    String fullpath = builder.toString().replace("//", "/");

                    Intent intent = new Intent(RepositoryContentFragment.INTENT_FILTER_CONTENT_CLICK);
                    intent.putExtra(RepositoryContentFragment.EXTRA_CONTENT_PATH, fullpath);
                    intent.putExtra(RepositoryContentFragment.EXTRA_CONTENT_NAME, item);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            });
        }

        void onBindViewHolder(int position) {
            String path = getItem(position);
            mTextView.setText(path);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (holder == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_content_path, parent, false);
            return new ItemViewHolder(itemView);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof ItemViewHolder)
            ((ItemViewHolder)holder).onBindViewHolder(position);
    }
}