package instinctools.android.adapters.search;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import instinctools.android.fragments.search.SearchFragment;
import instinctools.android.fragments.search.enums.SearchFragmentType;

public class SearchTypeAdapter extends FragmentStatePagerAdapter {
    public static final int NUM_PAGES = 4;

    SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();

    private final Context mContext;

    public SearchTypeAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        SearchFragment fragment;
        switch (SearchFragmentType.values()[position]) {
            case REPOSITORIES:
                fragment = SearchFragment.newInstance(SearchFragmentType.REPOSITORIES);
                break;
            case COMMITS:
                fragment = SearchFragment.newInstance(SearchFragmentType.COMMITS);
                break;
            case ISSUES:
                fragment = SearchFragment.newInstance(SearchFragmentType.ISSUES);
                break;
            case USERS:
                fragment = SearchFragment.newInstance(SearchFragmentType.USERS);
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
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return mRegisteredFragments.get(position);
    }
}
