package instinctools.android.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.activity.DescriptionRepositoryActivity;
import instinctools.android.database.DBConstants;
import instinctools.android.models.github.repositories.Repository;

public class RepositoryAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {
    private Context mContext;

    public static final String EXTRA_REPOSITORY_NAME_TAG = "REPOSITORY";

    public RepositoryAdapter(Context context, boolean showHeader, @Nullable Cursor cursor) {
        super(DBConstants.REPOSITORY_ID, context, showHeader, cursor);
        mContext = context;
    }

    private class RepositoryItemHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        private TextView mPrivateTextView;
        private TextView mLanguageTextView;
        private TextView mWatchTextView;
        private TextView mStarTextView;
        private TextView mForkTextView;
        private ImageView mRepositoryType;

        RepositoryItemHolder(View view) {
            super(view);

            mTitle = (TextView) view.findViewById(R.id.text_name);
            mDescription = (TextView) view.findViewById(R.id.text_description);
            mLanguageTextView = (TextView) view.findViewById(R.id.text_language);
            mWatchTextView = (TextView) view.findViewById(R.id.text_repo_watch);
            mStarTextView = (TextView) view.findViewById(R.id.text_repo_star);
            mForkTextView = (TextView) view.findViewById(R.id.text_repo_forks);
            mPrivateTextView = (TextView) view.findViewById(R.id.text_private_repository);
            mRepositoryType = (ImageView) view.findViewById(R.id.image_repository_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Repository repository = Repository.fromCursor(getCursor(getAdapterPosition()));
                    Intent intent = new Intent(mContext, DescriptionRepositoryActivity.class);
                    intent.putExtra(EXTRA_REPOSITORY_NAME_TAG, repository.getFullName());
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, 0, 0);
                    mContext.startActivity(intent, options.toBundle());
                }
            });
        }

        private void onBindViewHolder(Cursor cursor) {
            Repository item = Repository.fromCursor(cursor);

            if (item.isPrivate())
                mPrivateTextView.setVisibility(View.VISIBLE);
            else
                mPrivateTextView.setVisibility(View.GONE);

            if (item.isFork())
                mRepositoryType.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_github_repo_forked));
            else
                mRepositoryType.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_github_repo));

            mTitle.setText(item.getFullName());

            mWatchTextView.setText(String.valueOf(item.getWatchers()));
            mStarTextView.setText(String.valueOf(item.getStargazers()));
            mForkTextView.setText(String.valueOf(item.getForks()));

            if (TextUtils.isEmpty(item.getDescription()))
                mDescription.setVisibility(View.GONE);
            else {
                mDescription.setVisibility(View.VISIBLE);
                mDescription.setText(item.getDescription());
            }

            if (TextUtils.isEmpty(item.getLanguage()))
                mLanguageTextView.setVisibility(View.GONE);
            else {
                mLanguageTextView.setVisibility(View.VISIBLE);
                mLanguageTextView.setText(item.getLanguage());
            }
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_repository_header, parent, false);
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_repository, parent, false);
        return new RepositoryItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof RepositoryItemHolder)
                ((RepositoryItemHolder) holder).onBindViewHolder(cursor);
        }
    }
}