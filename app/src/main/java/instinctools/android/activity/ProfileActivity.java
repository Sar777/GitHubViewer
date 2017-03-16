package instinctools.android.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import instinctools.android.R;
import instinctools.android.adapters.profile.ProfileTypeAdapter;

public class ProfileActivity extends AppCompatActivity {
    public static final int LOADER_PROFILE_ID = 1;
    public static final int LOADER_ORGANIZATIONS_ID = 2;
    public static final int LOADER_EVENTS_ID = 3;
    public static final int LOADER_REPOSITORIES_ID = 4;

    public static final String EXTRA_USERNAME = "USERNAME";

    private String mUsername;

    // View
    private ViewPager mViewPager;
    private ProfileTypeAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getIntent() != null) {
            if (getIntent().getData() != null)
                mUsername = getIntent().getData().getAuthority();
            else
                mUsername = getIntent().getStringExtra(EXTRA_USERNAME);
        }

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_profile);

        mViewPager = (ViewPager) findViewById(R.id.pager_profile);
        mPagerAdapter = new ProfileTypeAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(ProfileTypeAdapter.NUM_PAGES);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    public String getUserName() {
        return mUsername;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
