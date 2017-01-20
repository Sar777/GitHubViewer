package instinctools.android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.imageloader.ImageLoadingStateListener;
import instinctools.android.loaders.AsyncUserInfoLoader;
import instinctools.android.misc.LinkTransformationMethod;
import instinctools.android.models.github.user.User;

public class ProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<User> {

    private static final int LOADER_PROFILE_ID = 1;
    private static final int REQUEST_CODE_AUTHORIZATION = 1;

    private ViewGroup mContentLayout;
    private ImageView mImageViewAvatar;
    private ProgressBar mProgressBar;
    private TextView mTextViewFullName;
    private TextView mTextViewPublicRepo;
    private TextView mTextViewPublicGists;
    private TextView mTextViewFollowers;
    private TextView mTextViewFollowing;
    private TextView mTextViewLocation;
    private TextView mTextViewEmail;
    private TextView mTextViewBlog;
    private TextView mTextViewOrganization;
    private TextView mTextViewBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();

        getSupportLoaderManager().initLoader(LOADER_PROFILE_ID, null, this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContentLayout = (ViewGroup) findViewById(R.id.layout_content_profile);
        mContentLayout.setVisibility(View.INVISIBLE);

        mImageViewAvatar = (ImageView) findViewById(R.id.image_profile_avatar);
        mImageViewAvatar.setVisibility(View.INVISIBLE);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_profile);
        mTextViewFullName = (TextView) findViewById(R.id.text_profile_fullname);
        mTextViewPublicRepo = (TextView) findViewById(R.id.text_profile_public_repo);
        mTextViewPublicGists = (TextView) findViewById(R.id.text_profile_public_gists);
        mTextViewFollowers = (TextView) findViewById(R.id.text_profile_followers);
        mTextViewFollowing = (TextView) findViewById(R.id.text_profile_following);
        mTextViewLocation = (TextView) findViewById(R.id.text_profile_location);
        mTextViewEmail = (TextView) findViewById(R.id.text_profile_email);

        mTextViewBlog = (TextView) findViewById(R.id.text_profile_blog);
        mTextViewBlog.setTransformationMethod(new LinkTransformationMethod());
        mTextViewBlog.setMovementMethod(LinkMovementMethod.getInstance());

        mTextViewOrganization = (TextView) findViewById(R.id.text_profile_organization);
        mTextViewBio = (TextView) findViewById(R.id.text_profile_bio);
    }

    @Override
    public Loader<User> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_PROFILE_ID)
            return null;

        return new AsyncUserInfoLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<User> loader, User user) {
        if (user == null) {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivityForResult(intent, REQUEST_CODE_AUTHORIZATION);
            return;
        }

        ImageLoader
                .what(user.getAvatarUrl())
                .in(mImageViewAvatar)
                .load(new ImageLoadingStateListener() {
                    @Override
                    public void onPrepare() {

                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onLoaded(Bitmap bitmap) {
                        mImageViewAvatar.setVisibility(View.VISIBLE);
                    }
                });

        getSupportActionBar().setWindowTitle(user.getName());

        mTextViewFullName.setText(String.format("@%s", user.getLogin()));
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
        mContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<User> loader) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_AUTHORIZATION)
            return;

        if (resultCode != RESULT_CANCELED) {
            finish();
            return;
        }

        getSupportLoaderManager().restartLoader(LOADER_PROFILE_ID, null, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
