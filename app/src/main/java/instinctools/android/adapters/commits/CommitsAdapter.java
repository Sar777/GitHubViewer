package instinctools.android.adapters.commits;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.imageloader.transformers.CircleImageTransformer;
import instinctools.android.models.github.commits.Commit;

public class CommitsAdapter extends AbstractRecyclerAdapter<Commit> {
    public CommitsAdapter(@NonNull Context context) {
        super(context);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewCommitterAvatar;
        private TextView mTextViewCommitterName;
        private TextView mTextViewCommitMessage;
        private TextView mTextViewCommitTimeAgo;
        private TextView mTextViewCommitHash;
        private TextView mTextViewCommitComments;

        ItemViewHolder(View view) {
            super(view);

            mImageViewCommitterAvatar = (ImageView) view.findViewById(R.id.image_committer_avatar);
            mTextViewCommitterName = (TextView) view.findViewById(R.id.text_committer_name);
            mTextViewCommitMessage = (TextView) view.findViewById(R.id.text_commit_message);
            mTextViewCommitTimeAgo = (TextView) view.findViewById(R.id.text_commit_time_ago);
            mTextViewCommitHash = (TextView) view.findViewById(R.id.text_commit_hash);
            mTextViewCommitComments = (TextView) view.findViewById(R.id.text_commit_comments);
        }

        void onBindViewHolder(int position) {
            Commit commit = getItem(position);

            ImageLoader
                    .what(commit.getAuthor().getAvatarUrl())
                    .in(mImageViewCommitterAvatar)
                    .transformer(new CircleImageTransformer())
                    .load();

            mTextViewCommitterName.setText(commit.getAuthor().getLogin());

            mTextViewCommitMessage.setText(commit.getCommitInfo().getMessage());
            mTextViewCommitTimeAgo.setText(DateUtils.getRelativeTimeSpanString(commit.getCommitInfo().getCommitter().getDate().getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
            mTextViewCommitHash.setText(commit.getSha());
            mTextViewCommitComments.setText(String.valueOf(commit.getCommitInfo().getCommentCount()));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (holder == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_commit, parent, false);
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