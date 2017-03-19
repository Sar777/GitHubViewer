package instinctools.android.adapters.contents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.models.github.contents.Content;

public class ContentsAdapter extends AbstractRecyclerAdapter<Content> {
    public ContentsAdapter(@NonNull Context context) {
        super(context);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewType;
        private TextView mTextViewName;
        private TextView mTextViewSize;

        ItemViewHolder(View view) {
            super(view);

            mImageViewType = (ImageView) view.findViewById(R.id.image_content_type);
            mTextViewName = (TextView) view.findViewById(R.id.text_content_name);
            mTextViewSize = (TextView) view.findViewById(R.id.text_content_size);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Content cont = getItem(getAdapterPosition());
                    // TODO
                }
            });
        }

        void onBindViewHolder(int position) {
            Content content = getItem(position);

            switch (content.getType()) {
                case FILE:
                    mImageViewType.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_github_file));
                    mTextViewSize.setVisibility(View.VISIBLE);
                    break;
                case DIR:
                    mImageViewType.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_github_directory));
                    mTextViewSize.setVisibility(View.GONE);
                    break;
                case SYMLINK:
                default:
                    throw new UnsupportedOperationException("Unsupported content type: " + content.getType());
            }

            mTextViewName.setText(content.getName());
            mTextViewSize.setText(Formatter.formatFileSize(mContext, content.getSize()));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (holder == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_content, parent, false);
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