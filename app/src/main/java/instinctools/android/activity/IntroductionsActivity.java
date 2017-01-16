package instinctools.android.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import instinctools.android.R;
import instinctools.android.adapters.IntroductionPagerAdapter;

public class IntroductionsActivity extends AppCompatActivity {

    private IntroductionPagerAdapter mIntroductionPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductions);

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mIntroductionPagerAdapter = new IntroductionPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mIntroductionPagerAdapter);
    }
}
