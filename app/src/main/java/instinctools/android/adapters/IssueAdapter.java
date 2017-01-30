package instinctools.android.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import instinctools.android.R;
import instinctools.android.models.github.issues.Issue;
import instinctools.android.models.github.issues.IssueState;

public class IssueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_VIEW_EMPTY = 0;
    private static final int TYPE_VIEW_ITEM = 1;

    private Context mContext;
    private List<Issue> mIssues;

    public IssueAdapter(Context context, @Nullable List<Issue> issues) {
        this.mContext = context;
        this.mIssues = issues;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewIssueState;
        TextView mTextViewIssueId;
        TextView mTextViewIssueTitle;

        ItemViewHolder(View view) {
            super(view);
            mTextViewIssueId = (TextView) view.findViewById(R.id.text_recycler_item_issue_id);
            mTextViewIssueState = (TextView) view.findViewById(R.id.text_recycler_item_issue_state);
            mTextViewIssueTitle = (TextView) view.findViewById(R.id.text_recycler_item_issue_title);
        }

        void onBindViewHolder(int position) {
            Issue issue = getItem(position);

            if (issue.getState() == IssueState.OPENED) {
                mTextViewIssueState.setText(mContext.getString(R.string.title_opened));
                mTextViewIssueState.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_issue_state_opened));
            }
            else {
                mTextViewIssueState.setText(mContext.getString(R.string.title_closed));
                mTextViewIssueState.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_issue_state_closed));
            }

            mTextViewIssueId.setText(String.format("#%s", issue.getNumber().toString()));
            mTextViewIssueTitle.setText(issue.getTitle());
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW_EMPTY)
            return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_issue_empty, parent, false));

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_issue, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder)
            ((ItemViewHolder)holder).onBindViewHolder(position);
    }

    @Override
    public int getItemCount() {
        return mIssues != null ? mIssues.size() : 1;
    }

    private Issue getItem(int position) {
        return mIssues.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mIssues == null || mIssues.isEmpty() ? TYPE_VIEW_EMPTY : TYPE_VIEW_ITEM;
    }

    public List<Issue> getIssues() {
        return mIssues;
    }

    public void setIssues(List<Issue> issues) {
        mIssues = issues;
    }
}