package instinctools.android.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    protected boolean mLoading = false; // True if we are still waiting for the last set of data to load.
    private int mVisibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int mLastVisibleItem, mVisibleItemCount, mTotalItemCount;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        mVisibleItemCount = recyclerView.getChildCount();
        mTotalItemCount = mLinearLayoutManager.getItemCount();
        mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

        if (mTotalItemCount <= 1)
            return;

        if (!mLoading && mTotalItemCount <= (mLastVisibleItem + mVisibleThreshold)) {
            mLoading = true;
            onLoadMore();
        }
    }

    public abstract void onLoadMore();
}
