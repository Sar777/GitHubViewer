package instinctools.android.adapters.follow;

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
import instinctools.android.models.github.user.UserShort;

public class FollowsAdapter extends AbstractRecyclerAdapter<UserShort> {
    private static final String TAG = "FollowsAdapter";

    public FollowsAdapter(@NonNull Context context) {
        super(context);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewAvatar;
        private TextView mTextViewLogin;
        private TextView mTextViewType;

        ItemViewHolder(View view) {
            super(view);

            mImageViewAvatar = (ImageView) view.findViewById(R.id.image_user_avatar);
            mTextViewLogin = (TextView) view.findViewById(R.id.text_user_login);
            mTextViewType = (TextView) view.findViewById(R.id.text_user_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserShort user = getItem(getAdapterPosition());
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.EXTRA_USERNAME, user.getLogin());
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, 0, 0);
                    mContext.startActivity(intent, options.toBundle());
                }
            });
        }

        void onBindViewHolder(int position) {
            UserShort user = getItem(position);

            ImageLoader
                    .what(user.getAvatarUrl())
                    .transformer(new CircleImageTransformer())
                    .in(mImageViewAvatar)
                    .load();

            mTextViewLogin.setText(user.getLogin());
            mTextViewType.setText(user.getType());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (holder == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_user, parent, false);
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