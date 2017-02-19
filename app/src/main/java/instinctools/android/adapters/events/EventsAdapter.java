package instinctools.android.adapters.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.imageloader.transformers.CircleImageTransformer;
import instinctools.android.models.github.events.Event;
import instinctools.android.models.github.events.payload.commit.PayloadCommit;
import instinctools.android.models.github.events.payload.release.PayloadAsset;
import instinctools.android.utility.CustomTextUtils;

public class EventsAdapter extends AbstractRecyclerAdapter<Event> {
    public EventsAdapter(@NonNull Context context) {
        super(context);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewAvatar;
        private ViewGroup mViewGroupBody;
        private TextView mTextViewActorName;
        private TextView mTextViewPostDate;
        private TextView mTextViewAction;
        private TextView mTextViewActionText;

        ItemViewHolder(View view) {
            super(view);

            mImageViewAvatar = (ImageView) view.findViewById(R.id.image_event_actor_avatar);
            mViewGroupBody = (ViewGroup) view.findViewById(R.id.layout_event_body);
            mTextViewActorName = (TextView) view.findViewById(R.id.text_event_actor_name);
            mTextViewPostDate = (TextView) view.findViewById(R.id.text_event_post_date);
            mTextViewAction = (TextView) view.findViewById(R.id.text_event_action);
            mTextViewAction.setMovementMethod(LinkMovementMethod.getInstance());

            mTextViewActionText = (TextView) view.findViewById(R.id.text_event_action_text);
            mTextViewActionText.setMovementMethod(LinkMovementMethod.getInstance());
        }

        void onBindViewHolder(int position) {
            Event event = getItem(position);

            mTextViewActorName.setText(event.getActor().getDisplayLogin());
            mTextViewPostDate.setText(DateUtils.getRelativeTimeSpanString(event.getCreatedAt().getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));

            ImageLoader
                    .what(event.getActor().getAvatarUrl())
                    .in(mImageViewAvatar)
                    .transformer(new CircleImageTransformer())
                    .load();

            mTextViewActionText.setVisibility(View.VISIBLE);
            mViewGroupBody.removeAllViews();

            String action;
            String actionText = null;
            switch (event.getType()) {
                case PUSH:
                    action = String.format("<a href='profile://%s'>%s</a> pushed to %s at <a href='repository://%s'>%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getPayload().getRef().split("/")[2], event.getRepo().getName(), event.getRepo().getName());
                    mTextViewActionText.setVisibility(View.GONE);

                    for (int i = 0; i < event.getPayload().getCommits().size() && i < 4; ++i) {
                        PayloadCommit commit = event.getPayload().getCommits().get(i);

                        View view = View.inflate(mContext, R.layout.item_recycler_event_commit, null);
                        // Title
                        TextView commitTitle = (TextView) view.findViewById(R.id.text_event_commit_title);
                        commitTitle.setText(commit.getMessage());
                        // Hash
                        TextView commitHash = (TextView) view.findViewById(R.id.text_event_commit_hash);
                        commitHash.setText(commit.getSha());
                        mViewGroupBody.addView(view);
                    }
                    break;
                case COMMIT_COMMENT:
                    action = String.format("<a href='profile://%s'>%s</a> commented on commit <a href='%s'>%s@%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getPayload().getComment().getHtmlUrl(), event.getRepo().getName(), event.getPayload().getComment().getCommentId().substring(0, 7));
                    actionText = event.getPayload().getComment().getBody();
                    break;
                case ISSUE_COMMENT:
                    action = String.format("<a href='profile://%s'>%s</a> commented on issue <a href='%s'>%s#%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getPayload().getIssue().getHtmlUrl(), event.getRepo().getName(), event.getPayload().getIssue().getNumber());
                    actionText = event.getPayload().getComment().getBody();
                    break;
                case PULL_REQUEST:
                    action = String.format("<a href='profile://%s'>%s</a> %s pull request <a href='%s'>%s#%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getPayload().getAction(), event.getPayload().getPullRequest().getHtmlUrl(), event.getRepo().getName(), event.getPayload().getPullRequest().getNumber());
                    actionText = event.getPayload().getPullRequest().getTitle();
                    break;
                case PULL_REQUEST_REVIEW_COMMENT:
                    action = String.format("<a href='profile://%s'>%s</a> commented on pull request <a href='%s'>%s#%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getPayload().getPullRequest().getHtmlUrl(), event.getRepo().getName(), event.getPayload().getPullRequest().getNumber());
                    actionText = event.getPayload().getComment().getBody();
                    break;
                case ISSUES:
                    action = String.format("<a href='profile://%s'>%s</a> %s issue <a href='%s'>%s#%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getPayload().getAction(), event.getPayload().getIssue().getHtmlUrl(), event.getRepo().getName(), event.getPayload().getIssue().getNumber());
                    actionText = event.getPayload().getIssue().getTitle();
                    break;
                case FORK:
                    action = String.format("<a href='profile://%s'>%s</a> forked <a href='repository://%s'>%s</a> to <a href='repository://%s'>%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getRepo().getName(), event.getRepo().getName(), event.getPayload().getForkee().getFullName(), event.getPayload().getForkee().getFullName());
                    mTextViewActionText.setVisibility(View.GONE);
                    break;
                case RELEASE: {
                    action = String.format("<a href='profile://%s'>%s</a> released %s at <a href='repository://%s'>%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getPayload().getRelease().getName(), event.getRepo().getName(), event.getRepo().getName());
                    mTextViewActionText.setVisibility(View.GONE);

                    for (PayloadAsset asset : event.getPayload().getRelease().getAssets()) {
                        View view = View.inflate(mContext, R.layout.item_recycler_event_release, null);

                        TextView assetName = (TextView) view.findViewById(R.id.text_event_asset_name);
                        assetName.setText(asset.getName());
                        mViewGroupBody.addView(view);
                    }
                    break;
                }
                case CREATE:
                    action = String.format("<a href='profile://%s'>%s</a> created %s %s at <a href='repository://%s'>%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getPayload().getRefType(), event.getPayload().getRef(), event.getRepo().getName(), event.getRepo().getName());
                    mTextViewActionText.setVisibility(View.GONE);
                    break;
                case DELETE:
                    action = String.format("<a href='profile://%s'>%s</a> deleted %s %s at <a href='repository://%s'>%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getPayload().getRefType(), event.getPayload().getRef(), event.getRepo().getName(), event.getRepo().getName());
                    mTextViewActionText.setVisibility(View.GONE);
                    break;
                case WATCH:
                    action = String.format("<a href='profile://%s'>%s</a> starred <a href='repository://%s'>%s</a>", event.getActor().getLogin(), event.getActor().getDisplayLogin(), event.getRepo().getName(), event.getRepo().getName());
                    mTextViewActionText.setVisibility(View.GONE);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported event type: " + event.getType());
            }

            mTextViewAction.setText(CustomTextUtils.fromHtml(action));
            mTextViewActionText.setText(actionText);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (holder == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_event, parent, false);
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