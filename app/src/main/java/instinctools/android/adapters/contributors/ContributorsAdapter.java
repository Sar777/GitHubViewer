package instinctools.android.adapters.contributors;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.activity.ProfileActivity;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.imageloader.transformers.CircleImageTransformer;
import instinctools.android.models.github.user.UserContributor;

public class ContributorsAdapter extends AbstractRecyclerAdapter<UserContributor> {
    public ContributorsAdapter(@NonNull Context context) {
        super(context);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewAvatar;
        private TextView mTextViewName;
        private TextView mTextViewCount;

        ItemViewHolder(View view) {
            super(view);

            mImageViewAvatar = (ImageView) view.findViewById(R.id.image_contributor_avatar);
            mImageViewAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.EXTRA_USERNAME, mTextViewName.getText());
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(mImageViewAvatar, 0, 0, 0, 0);
                    mContext.startActivity(intent, options.toBundle());
                }
            });

            mTextViewName = (TextView) view.findViewById(R.id.text_contributor_name);
            mTextViewCount = (TextView) view.findViewById(R.id.text_contributors_count);
        }

        void onBindViewHolder(int position) {
            UserContributor user = getItem(position);

            ImageLoader
                    .what(user.getAvatarUrl())
                    .in(mImageViewAvatar)
                    .transformer(new CircleImageTransformer())
                    .load();

            if (user.getLogin().equals("invalid-email-address"))
                mImageViewAvatar.setClickable(false);
            else
                mImageViewAvatar.setClickable(true);

            mTextViewName.setText(user.getLogin());
            mTextViewCount.setText(String.format(mContext.getString(R.string.msg_contributions), user.getContributions()));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (holder == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_contributor, parent, false);
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