package instinctools.android.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import instinctools.android.R;
import instinctools.android.activity.AuthActivity;
import instinctools.android.activity.FollowActivity;
import instinctools.android.activity.ProfileActivity;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.imageloader.transformers.CircleImageTransformer;
import instinctools.android.loaders.organizations.AsyncOrganizationsListLoader;
import instinctools.android.loaders.user.AsyncUserInfoLoader;
import instinctools.android.misc.LinkTransformationMethod;
import instinctools.android.models.github.follow.FollowType;
import instinctools.android.models.github.organizations.Organization;
import instinctools.android.models.github.user.User;

import static android.app.Activity.RESULT_CANCELED;

public class ProfileAboutFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final int REQUEST_CODE_AUTHORIZATION = 1;

    public static final String EXTRA_FOLLOW_TYPE = "FOLLOW_TYPE";
    public static final String EXTRA_USERNAME = "USERNAME";

    // View
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewGroup mViewGroupProfileInfo;
    private ViewGroup mOrganizationsContainer;
    private CardView mCardViewOrganizations;
    private ImageView mImageViewAvatar;
    private ProgressBar mProgressBar;
    private TextView mTextViewPublicRepo;
    private TextView mTextViewPublicGists;
    private TextView mTextViewFollowers;
    private TextView mTextViewFollowing;
    private TextView mTextViewLocation;
    private TextView mTextViewEmail;
    private TextView mTextViewBlog;
    private TextView mTextViewOrganization;
    private TextView mTextViewBio;

    private TextView mTextViewFollowersAction;
    private TextView mTextViewFollowingAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_about, null);

        mViewGroupProfileInfo = (ViewGroup) view.findViewById(R.id.layout_profile_about_info);
        mViewGroupProfileInfo.setVisibility(View.INVISIBLE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_profile_about);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mCardViewOrganizations = (CardView) view.findViewById(R.id.cardview_profile_organizations);
        mCardViewOrganizations.setVisibility(View.GONE);
        mOrganizationsContainer = (ViewGroup) view.findViewById(R.id.layout_profile_organization_container);
        mImageViewAvatar = (ImageView) view.findViewById(R.id.image_profile_avatar);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_profile);
        mTextViewPublicRepo = (TextView) view.findViewById(R.id.text_profile_public_repo);
        mTextViewPublicGists = (TextView) view.findViewById(R.id.text_profile_public_gists);
        mTextViewFollowers = (TextView) view.findViewById(R.id.text_profile_followers);
        mTextViewFollowing = (TextView) view.findViewById(R.id.text_profile_following);
        mTextViewLocation = (TextView) view.findViewById(R.id.text_profile_location);
        mTextViewEmail = (TextView) view.findViewById(R.id.text_profile_email);

        mTextViewBlog = (TextView) view.findViewById(R.id.text_profile_blog);
        mTextViewBlog.setTransformationMethod(new LinkTransformationMethod());
        mTextViewBlog.setMovementMethod(LinkMovementMethod.getInstance());

        mTextViewOrganization = (TextView) view.findViewById(R.id.text_profile_organization);
        mTextViewBio = (TextView) view.findViewById(R.id.text_profile_bio);

        mTextViewFollowersAction = (TextView) view.findViewById(R.id.text_profile_followers_action);
        mTextViewFollowersAction.setOnClickListener(this);
        mTextViewFollowingAction = (TextView) view.findViewById(R.id.text_profile_following_action);
        mTextViewFollowingAction.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().getSupportLoaderManager().initLoader(ProfileActivity.LOADER_PROFILE_ID, Bundle.EMPTY, this);
        getActivity().getSupportLoaderManager().initLoader(ProfileActivity.LOADER_ORGANIZATIONS_ID, Bundle.EMPTY, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSupportLoaderManager().destroyLoader(ProfileActivity.LOADER_PROFILE_ID);
        getActivity().getSupportLoaderManager().destroyLoader(ProfileActivity.LOADER_ORGANIZATIONS_ID);
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        Loader<? extends Object> loader;
        switch (id) {
            case ProfileActivity.LOADER_PROFILE_ID:
                loader = new AsyncUserInfoLoader(getContext(), ((ProfileActivity)getActivity()).getUserName());
                break;
            case ProfileActivity.LOADER_ORGANIZATIONS_ID:
                loader = new AsyncOrganizationsListLoader(getContext(), ((ProfileActivity)getActivity()).getUserName());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported loader id: " + id);

        }

        return (Loader<Object>)loader;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object object) {
        if (loader.getId() == ProfileActivity.LOADER_PROFILE_ID)
            initialUserInfo((User)object);
        else if (loader.getId() == ProfileActivity.LOADER_ORGANIZATIONS_ID)
            initialOrganizationsInfo((List<Organization>)object);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_AUTHORIZATION)
            return;

        if (resultCode != RESULT_CANCELED) {
            getActivity().finish();
            return;
        }

        getActivity().getSupportLoaderManager().restartLoader(ProfileActivity.LOADER_PROFILE_ID, Bundle.EMPTY, this);
        getActivity().getSupportLoaderManager().restartLoader(ProfileActivity.LOADER_ORGANIZATIONS_ID, Bundle.EMPTY, this);
    }

    private void initialUserInfo(User user) {
        if (user == null) {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            startActivityForResult(intent, REQUEST_CODE_AUTHORIZATION);
            return;
        }

        ImageLoader
                .what(user.getAvatarUrl())
                .in(mImageViewAvatar)
                .transformer(new CircleImageTransformer())
                .load();

        ProfileActivity activity = (ProfileActivity)getActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("None".equals(user.getName()) ? user.getLogin() : user.getName());
            activity.getSupportActionBar().setSubtitle(String.format("@%s", user.getLogin()));
        }

        mTextViewPublicRepo.setText(String.valueOf(user.getPublicRepos()));
        mTextViewPublicGists.setText(String.valueOf(user.getPublicGists()));
        mTextViewFollowers.setText(user.getFollowers());
        mTextViewFollowing.setText(user.getFollowing());
        mTextViewLocation.setText(user.getLocation());
        mTextViewEmail.setText(user.getEmail());

        if (!TextUtils.isEmpty(user.getCompany()))
            mTextViewOrganization.setText(user.getCompany());
        else
            mTextViewOrganization.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(user.getBlog()))
            mTextViewBlog.setText(user.getBlog());
        else
            mTextViewBlog.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(user.getBio()))
            mTextViewBio.setText(user.getBio());
        else
            mTextViewBio.setVisibility(View.GONE);

        mProgressBar.setVisibility(View.GONE);
        mViewGroupProfileInfo.setVisibility(View.VISIBLE);
    }

    private void initialOrganizationsInfo(List<Organization> organizations) {
        if (organizations.isEmpty())
            return;

        mOrganizationsContainer.removeAllViews();

        for (Organization organization : organizations) {
            View view = View.inflate(getContext(), R.layout.cardview_profile_organizations, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_organization_avatar);
            TextView textView = (TextView) view.findViewById(R.id.text_organization_name);

            ImageLoader
                    .what(organization.getAvatarUrl())
                    .in(imageView)
                    .transformer(new CircleImageTransformer())
                    .load();

            textView.setText(organization.getLogin());
            mOrganizationsContainer.addView(view);
        }

        mCardViewOrganizations.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        getActivity().getSupportLoaderManager().getLoader(ProfileActivity.LOADER_PROFILE_ID).forceLoad();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_profile_following_action:
            case R.id.text_profile_followers_action: {
                Intent intent = new Intent(getContext(), FollowActivity.class);
                intent.putExtra(EXTRA_FOLLOW_TYPE, v.getId() == R.id.text_profile_followers_action ? FollowType.Followers : FollowType.Following);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
}
