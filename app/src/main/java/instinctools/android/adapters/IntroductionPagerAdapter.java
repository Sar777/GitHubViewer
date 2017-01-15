package instinctools.android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import instinctools.android.fragments.introductions.AboutGithubFragment;

public class IntroductionPagerAdapter extends FragmentPagerAdapter {

    private static final int MAX_PAGES = 4;

    public IntroductionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return AboutGithubFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return MAX_PAGES;
    }
}