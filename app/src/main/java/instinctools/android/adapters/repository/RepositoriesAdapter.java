package instinctools.android.adapters.repository;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.activity.RepositoryDescriptionActivity;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.models.github.repositories.Repository;

public class RepositoriesAdapter extends AbstractRecyclerAdapter<Repository> {
    private static final String TAG = "RepositoriesAdapter";

    public static final String EXTRA_REPOSITORY_NAME_TAG = "REPOSITORY";

    private boolean mSelf;

    public RepositoriesAdapter(@NonNull Context context, boolean self) {
        super(context);

        this.mSelf = self;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        private TextView mPrivateTextView;
        private TextView mLanguageTextView;
        private TextView mWatchTextView;
        private TextView mStarTextView;
        private TextView mForkTextView;
        private ImageView mRepositoryType;

        ItemViewHolder(View view) {
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
                    Repository repository = getItem(getAdapterPosition());
                    Intent intent = new Intent(mContext, RepositoryDescriptionActivity.class);
                    intent.putExtra(EXTRA_REPOSITORY_NAME_TAG, repository.getFullName());
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, 0, 0);
                    mContext.startActivity(intent, options.toBundle());
                }
            });
        }

        private void onBindViewHolder(int position) {
            Repository item = getItem(position);

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (holder == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_repository, parent, false);
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