package instinctools.android.adapters.notifications;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import instinctools.android.R;
import instinctools.android.fragments.notifications.NotificationFragment;
import instinctools.android.fragments.notifications.NotificationFragmentType;

public class NotificationTypeAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 3;

    private final Context mContext;

    public NotificationTypeAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        NotificationFragment fragment = null;
        switch (position) {
            case 0:
                fragment = NotificationFragment.newInstance(NotificationFragmentType.UNREAD);
                break;
            case 1:
                fragment = NotificationFragment.newInstance(NotificationFragmentType.PARTICIPATING);
                break;
            case 2:
                fragment = NotificationFragment.newInstance(NotificationFragmentType.ALL);
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
                title = mContext.getString(R.string.title_notification_tab_unread);
                break;
            case 1:
                title = mContext.getString(R.string.title_notification_tab_participating);
                break;
            case 2:
                title = mContext.getString(R.string.title_notification_tab_all);
                break;
            default:
                break;
        }

        return title;
    }
}
