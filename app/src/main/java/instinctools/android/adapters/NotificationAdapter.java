package instinctools.android.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.database.DBConstants;
import instinctools.android.database.providers.NotificationsProvider;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.services.github.GithubServiceListener;
import instinctools.android.services.github.notification.GithubNotifications;

public class NotificationAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> implements View.OnClickListener, ItemTouchHelperAdapter  {
    private Context mContext;
    private RecyclerView mRecyclerView;

    public static final String EXTRA_NOTIFICATION_ID_TAG = "NOTIFICATION";

    public NotificationAdapter(Context context, RecyclerView recyclerView, boolean showHeader, @Nullable Cursor cursor) {
        super(DBConstants.REPOSITORY_ID, context, showHeader, cursor);
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    public void onClick(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
    }

    @Override
    public void onItemDismiss(final int position) {
        if (position == 0 && getItemCount() == 0)
            return;

        Notification notification = Notification.fromCursor(getCursor(position));
        GithubNotifications.markNotification(notification.getUrl(), new GithubServiceListener<Boolean>() {
            @Override
            public void onError(int code, ErrorResponse response) {
                Snackbar.make(mRecyclerView, R.string.msg_unknown_error_mark_notification, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Boolean success) {
                if (!success)
                    return;

                mContext.getContentResolver().delete(NotificationsProvider.NOTIFICATIONS_CONTENT_URI, DBConstants.NOTIFICATION_ID + " = ?", new String[] {String.valueOf(getItemId(position))});
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
    }

    private class NotificationItemHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewSubject;
        private TextView mTextViewRepo;
        private ImageView mImageView;

        NotificationItemHolder(View view) {
            super(view);

            mTextViewSubject = (TextView) view.findViewById(R.id.text_notification_text);
            mTextViewRepo = (TextView) view.findViewById(R.id.text_notification_repository);
            mImageView = (ImageView) view.findViewById(R.id.image_notification_type);
        }

        private void onBindViewHolder(Cursor cursor) {
            Notification item = Notification.fromCursor(cursor);
            mTextViewSubject.setText(item.getSubject().getTitle());
            mTextViewRepo.setText(item.getRepository().getFullName());

            int resId;
            switch (item.getSubject().getType()) {
                case ISSUE:
                    resId = R.drawable.ic_github_issue_opened;
                    break;
                case PULLREQUEST:
                    resId = R.drawable.ic_github_repo_pull_request;
                    break;
                case COMMIT:
                    resId = R.drawable.ic_github_commit;
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported notification type: " + item.getSubject().getType());
            }

            mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, resId));
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_notification_header, parent, false);
                return new HeaderItemHolder(view);
            }
            case VIEW_TYPE_EMPTY: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_notification_empty, parent, false);
                return new EmptyItemHolder(view);
            }
            case VIEW_TYPE_ITEM:
                break;
            default:
                throw new UnsupportedOperationException("Unsupported view item type: " + viewType);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_notification, parent, false);
        view.setOnClickListener(this);
        return new NotificationItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof NotificationItemHolder)
                ((NotificationItemHolder) holder).onBindViewHolder(cursor);
        }
    }
}