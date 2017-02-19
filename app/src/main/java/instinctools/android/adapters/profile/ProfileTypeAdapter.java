package instinctools.android.adapters.profile;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import instinctools.android.R;
import instinctools.android.fragments.profile.ProfileAboutFragment;
import instinctools.android.fragments.profile.ProfileEventsFragment;

public class ProfileTypeAdapter extends FragmentStatePagerAdapter {
    public static final int NUM_PAGES = 2;

    private final Context mContext;

    public ProfileTypeAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new ProfileAboutFragment();
                break;
            case 1:
                fragment = new ProfileEventsFragment();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported search fragment type by position: " + position);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_profile_tab_about);
            case 1:
                return mContext.getString(R.string.title_profile_tab_events);
        }

        return null;
    }
}
