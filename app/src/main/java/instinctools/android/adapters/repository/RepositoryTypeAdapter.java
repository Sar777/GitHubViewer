package instinctools.android.adapters.repository;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import instinctools.android.R;
import instinctools.android.fragments.repository.RepositoryAboutFragment;
import instinctools.android.fragments.repository.RepositoryCommitsFragment;

public class RepositoryTypeAdapter extends FragmentStatePagerAdapter {
    public static final int NUM_PAGES = 2;

    private final Context mContext;

    public RepositoryTypeAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new RepositoryAboutFragment();
                break;
            case 1:
                fragment = new RepositoryCommitsFragment();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported repository fragment type by position: " + position);
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
                return mContext.getString(R.string.title_repository_description_tab_information);
            case 1:
                return mContext.getString(R.string.title_repository_description_tab_commits);
        }

        return null;
    }
}
