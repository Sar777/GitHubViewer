package instinctools.android.adapters.issues;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.imageloader.transformers.CircleImageTransformer;
import instinctools.android.models.github.issues.Issue;

public class IssuesAdapter extends AbstractRecyclerAdapter<Issue> {
    private static final String TAG = "IssuesAdapter";

    public IssuesAdapter(@NonNull Context context) {
        super(context);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewAuthorAvatar;
        private TextView mTextViewIssueAuthorName;
        private TextView mTextViewIssueTitle;
        private TextView mTextViewIssueCreatedTimeAgo;
        private TextView mTextViewIssueNumber;
        private TextView mTextViewIssueComments;

        ItemViewHolder(View view) {
            super(view);

            mImageViewAuthorAvatar = (ImageView) view.findViewById(R.id.image_issue_author_avatar);
            mTextViewIssueAuthorName = (TextView) view.findViewById(R.id.text_issue_author_name);
            mTextViewIssueTitle = (TextView) view.findViewById(R.id.text_issue_title);
            mTextViewIssueCreatedTimeAgo = (TextView) view.findViewById(R.id.text_issue_created_time_ago);
            mTextViewIssueNumber = (TextView) view.findViewById(R.id.text_issue_number);
            mTextViewIssueComments = (TextView) view.findViewById(R.id.text_issue_comments);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Issue issue = getItem(getAdapterPosition());
                    App.launchUrl(mContext, issue.getHtmlUrl());
                }
            });
        }

        void onBindViewHolder(int position) {
            Issue issue = getItem(position);

            ImageLoader
                    .what(issue.getUser().getAvatarUrl())
                    .in(mImageViewAuthorAvatar)
                    .transformer(new CircleImageTransformer())
                    .load();

            mTextViewIssueAuthorName.setText(issue.getUser().getLogin());

            mTextViewIssueTitle.setText(issue.getTitle());
            mTextViewIssueCreatedTimeAgo.setText(DateUtils.getRelativeTimeSpanString(issue.getCreatedAt().getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
            mTextViewIssueNumber.setText(String.valueOf(issue.getNumber()));
            mTextViewIssueComments.setText(String.valueOf(issue.getComments()));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (holder == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_issue, parent, false);
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