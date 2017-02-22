package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import instinctools.android.R;
import instinctools.android.adapters.profile.ProfileTypeAdapter;
import instinctools.android.adapters.repository.RepositoryAdapter;
import instinctools.android.adapters.repository.RepositoryTypeAdapter;

public class RepositoryDescriptionActivity extends AppCompatActivity {
    public static final String EXTRA_REPOSITORY_FULLNAME = "FULLNAME";

    public static final int LOADER_REPOSITORY_ABOUT_ID = 1;
    public static final int LOADER_REPOSITORY_COMMITS_ID = 2;

    private String mFullName;

    // View
    private ViewPager mViewPager;
    private RepositoryTypeAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_description);

        initView();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getData() != null)
                mFullName = intent.getData().toString().split("//")[1];
            else
                mFullName = intent.getStringExtra(RepositoryAdapter.EXTRA_REPOSITORY_NAME_TAG);
        }
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_repository);

        mViewPager = (ViewPager) findViewById(R.id.pager_repository);
        mPagerAdapter = new RepositoryTypeAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(ProfileTypeAdapter.NUM_PAGES);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public String getFullName() {
        return mFullName;
    }
}
