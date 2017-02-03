package instinctools.android.adapters.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.models.github.issues.Issue;
import instinctools.android.models.github.issues.IssueState;

public class SearchIssuesAdapter extends AbstractSearchAdapter<Issue> {
    public SearchIssuesAdapter(@NonNull Context context) {
        super(context);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextViewTitle;
        private TextView mTextViewBody;
        private TextView mTextViewComments;
        private TextView mTextViewCreatedAt;

        ItemViewHolder(View view) {
            super(view);

            mImageView = (ImageView) view.findViewById(R.id.image_issue_state);
            mTextViewTitle = (TextView) view.findViewById(R.id.text_issue_title);
            mTextViewBody = (TextView) view.findViewById(R.id.text_issue_body);
            mTextViewComments = (TextView) view.findViewById(R.id.text_issue_comments);
            mTextViewCreatedAt = (TextView) view.findViewById(R.id.text_issue_created);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Issue issue = getItem(getAdapterPosition());
                    App.launchUrl(mContext, issue.getHtmlUrl());
                }
            });
        }

        void onBindViewHolder(int position) {
            Issue issue = getItem(position);

            mTextViewTitle.setText(TextUtils.isEmpty(issue.getTitle()) ? "Empty" : issue.getTitle());
            mTextViewBody.setText(TextUtils.isEmpty(issue.getBody()) ? "Empty" : issue.getBody());
            mTextViewComments.setText(String.valueOf(issue.getComments()));

            if (issue.getPullRequest() == null)
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_github_issue_opened));
            else
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_github_repo_pull_request));

            if (issue.getState() == IssueState.CLOSED)
                mImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.github_octicon_red));
            else
                mImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.github_octicon_green));

            String createdDate = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(issue.getCreatedAt());
            mTextViewCreatedAt.setText(createdDate);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW_EMPTY)
            return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_repository_empty, parent, false));

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_search_issue, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder)
            ((ItemViewHolder)holder).onBindViewHolder(position);
    }
}