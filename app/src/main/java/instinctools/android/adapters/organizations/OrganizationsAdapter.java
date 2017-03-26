package instinctools.android.adapters.organizations;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.imageloader.transformers.CircleImageTransformer;
import instinctools.android.models.github.organizations.Organization;
import instinctools.android.utility.CustomTextUtils;

public class OrganizationsAdapter extends AbstractRecyclerAdapter<Organization> {
    public OrganizationsAdapter(@NonNull Context context) {
        super(context);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewAvatar;
        private TextView mTextViewLogin;
        private TextView mTextViewDescription;

        ItemViewHolder(View view) {
            super(view);

            mImageViewAvatar = (ImageView) view.findViewById(R.id.image_organization_avatar);
            mTextViewLogin = (TextView) view.findViewById(R.id.text_organization_login);
            mTextViewDescription = (TextView) view.findViewById(R.id.text_organization_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  Intent intent = new Intent(mContext, ProfileActivity.class);
                 //   intent.putExtra(ProfileActivity.EXTRA_USERNAME, mTextViewLogin.getText());
                 //   ActivityOptions options = ActivityOptions.makeScaleUpAnimation(mImageViewAvatar, 0, 0, 0, 0);
                 //   mContext.startActivity(intent, options.toBundle());
                }
            });
        }

        void onBindViewHolder(int position) {
            Organization org = getItem(position);

            ImageLoader
                    .what(org.getAvatarUrl())
                    .in(mImageViewAvatar)
                    .transformer(new CircleImageTransformer())
                    .load();

            mTextViewLogin.setText(org.getLogin());

            if (!CustomTextUtils.isEmpty(org.getDescription())) {
                mTextViewDescription.setText(org.getDescription());
                mTextViewDescription.setVisibility(View.VISIBLE);
            }
            else
                mTextViewDescription.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (holder == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_organization, parent, false);
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