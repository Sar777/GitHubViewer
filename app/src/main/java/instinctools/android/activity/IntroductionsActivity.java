package instinctools.android.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import instinctools.android.R;
import instinctools.android.adapters.IntroductionPagerAdapter;

public class IntroductionsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private IntroductionPagerAdapter mIntroductionPagerAdapter;
    private ViewPager mViewPager;

    private Button mButtonSlides[];

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
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(mIntroductionPagerAdapter);

        int width = (int) getResources().getDimension(R.dimen.introduction_slide_button_width_height);
        int heigth = (int) getResources().getDimension(R.dimen.introduction_slide_button_width_height);
        int margin = (int) getResources().getDimension(R.dimen.introduction_slide_button_margin);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.layout_slide_buttons);

        mButtonSlides = new Button[IntroductionPagerAdapter.MAX_PAGES];
        for (int i = 0; i < IntroductionPagerAdapter.MAX_PAGES; ++i) {
            Button button = new Button(this);
            button.setTag(i);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_cell));
            button.setOnClickListener(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, heigth);
            layoutParams.setMargins(margin, margin, margin, margin);
            button.setLayoutParams(layoutParams);

            viewGroup.addView(button);
            mButtonSlides[i] = button;
        }

        onPageSelected(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int width = (int) getResources().getDimension(R.dimen.introduction_slide_button_width_height);
        int height = (int) getResources().getDimension(R.dimen.introduction_slide_button_width_height);

        for (int i = 0; i < IntroductionPagerAdapter.MAX_PAGES; ++i) {
            ViewGroup.LayoutParams layoutParams = mButtonSlides[i].getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            mButtonSlides[i].setLayoutParams(layoutParams);
        }

        ViewGroup.LayoutParams layoutParams = mButtonSlides[position].getLayoutParams();
        layoutParams.width = (int) ((int) width * 1.3);
        layoutParams.height = (int) ((int) width * 1.3);
        mButtonSlides[position].setLayoutParams(layoutParams);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        mViewPager.setCurrentItem(position);
    }
}
