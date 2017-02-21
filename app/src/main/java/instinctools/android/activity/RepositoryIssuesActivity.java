package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import instinctools.android.R;
import instinctools.android.adapters.issues.IssueTypeAdapter;
import instinctools.android.adapters.profile.ProfileTypeAdapter;

public class RepositoryIssuesActivity extends AppCompatActivity {
    // View
    private ViewPager mViewPager;
    private IssueTypeAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    private String mFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_issues);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        mFullName = intent.getStringExtra(RepositoryDescriptionActivity.EXTRA_REPOSITORY_FULLNAME);
        if (TextUtils.isEmpty(mFullName)) {
            finish();
            return;
        }

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle(mFullName);
        setSupportActionBar(toolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_issues);

        mViewPager = (ViewPager) findViewById(R.id.pager_issues);
        mPagerAdapter = new IssueTypeAdapter(this, getSupportFragmentManager());
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
