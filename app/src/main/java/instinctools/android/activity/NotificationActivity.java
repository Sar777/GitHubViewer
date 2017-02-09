package instinctools.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import instinctools.android.App;
import instinctools.android.R;
import instinctools.android.adapters.NotificationTypeAdapter;

public class NotificationActivity extends AppCompatActivity {
    public static final String NOTIFICATION_URL = "NOTIFICATION_URL";

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initView();

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null)
            return;

        if (extras.containsKey(NOTIFICATION_URL))
            App.launchUrl(this, extras.getString(NOTIFICATION_URL));
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_notifications);

        mViewPager = (ViewPager) findViewById(R.id.pager_notifications);
        mPagerAdapter = new NotificationTypeAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
