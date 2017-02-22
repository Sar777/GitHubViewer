package instinctools.android.adapters.issues;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import instinctools.android.R;
import instinctools.android.fragments.issues.RepositoryIssueFragment;
import instinctools.android.models.github.issues.IssueState;

public class IssueTypeAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 2;

    private final Context mContext;

    public IssueTypeAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = RepositoryIssueFragment.newInstance(IssueState.OPENED);
                break;
            case 1:
                fragment = RepositoryIssueFragment.newInstance(IssueState.CLOSED);
                break;
            default:
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = null;
        switch (position) {
            case 0:
                title = mContext.getString(R.string.title_repository_issues_tab_opened);
                break;
            case 1:
                title = mContext.getString(R.string.title_repository_issues_tab_closed);
                break;
            default:
                break;
        }

        return title;
    }
}
