package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import instinctools.android.R;
import instinctools.android.imageloader.ImageLoader;
import instinctools.android.loaders.AsyncUserInfoLoader;
import instinctools.android.models.github.user.User;

public class ProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<User> {

    private static final int LOADER_PROFILE_ID = 1;
    private static final int REQUEST_CODE_AUTHORIZATION = 1;

    private ViewGroup mContentLayout;
    private ProgressBar mProgressBar;

    private ImageView mImageViewAvatar;
    private TextView mTextViewLogin;
    private TextView mTextViewUserType;
    private TextView mTextViewUsername;
    private TextView mTextViewLocation;
    private TextView mTextViewEmail;
    private TextView mTextViewBio;
    private TextView mTextViewPublicRepo;
    private TextView mTextViewPublicGists;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContentLayout = (ViewGroup) findViewById(R.id.layout_content_profile);
        mContentLayout.setVisibility(View.INVISIBLE);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_profile);

        mImageViewAvatar = (ImageView) findViewById(R.id.image_profile_avatar);
        mTextViewLogin = (TextView) findViewById(R.id.text_profile_login);
        mTextViewUserType = (TextView) findViewById(R.id.text_profile_user_type);
        mTextViewUsername = (TextView) findViewById(R.id.text_profile_username);
        mTextViewLocation = (TextView) findViewById(R.id.text_profile_location);
        mTextViewEmail = (TextView) findViewById(R.id.text_profile_email);
        mTextViewBio = (TextView) findViewById(R.id.text_profile_bio);
        mTextViewPublicRepo = (TextView) findViewById(R.id.text_profile_public_repo);
        mTextViewPublicGists = (TextView) findViewById(R.id.text_profile_public_gists);
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
                .load();

        getSupportActionBar().setWindowTitle(user.getName());

        mTextViewLogin.setText(user.getLogin());
        mTextViewUserType.setText(user.getType());
        mTextViewUsername.setText(user.getName());
        mTextViewLocation.setText(user.getLocation());
        mTextViewEmail.setText(user.getEmail());
        mTextViewBio.setText(user.getBio());
        mTextViewPublicRepo.setText(String.valueOf(user.getPublicRepos()));
        mTextViewPublicGists.setText(String.valueOf(user.getPublicGists()));

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
