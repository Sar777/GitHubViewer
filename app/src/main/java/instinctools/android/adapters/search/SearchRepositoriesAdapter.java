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

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.models.github.repositories.Repository;

public class SearchRepositoriesAdapter extends AbstractSearchAdapter<Repository> {
    public SearchRepositoriesAdapter(@NonNull Context context, @NonNull RecyclerView recyclerView) {
        super(context, recyclerView);
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
                    App.launchUrl(mContext, repository.getHtmlUrl());
                }
            });
        }

        void onBindViewHolder(int position) {
            Repository repository = getItem(position);

            if (repository.isPrivate())
                mPrivateTextView.setVisibility(View.VISIBLE);
            else
                mPrivateTextView.setVisibility(View.GONE);

            if (repository.isFork())
                mRepositoryType.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_github_repo_forked));
            else
                mRepositoryType.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_github_repo));

            mTitle.setText(repository.getFullName());

            mWatchTextView.setText(String.valueOf(repository.getWatchers()));
            mStarTextView.setText(String.valueOf(repository.getStargazers()));
            mForkTextView.setText(String.valueOf(repository.getForks()));

            if (TextUtils.isEmpty(repository.getDescription()))
                mDescription.setVisibility(View.GONE);
            else {
                mDescription.setVisibility(View.VISIBLE);
                mDescription.setText(repository.getDescription());
            }

            if (TextUtils.isEmpty(repository.getLanguage()))
                mLanguageTextView.setVisibility(View.GONE);
            else {
                mLanguageTextView.setVisibility(View.VISIBLE);
                mLanguageTextView.setText(repository.getLanguage());
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