package instinctools.android.adapters.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.R;
import instinctools.android.listeners.OnItemClickListener;
import instinctools.android.listeners.OnLoadMoreListener;

public abstract class AbstractSearchAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_VIEW_EMPTY = 0;
    protected static final int TYPE_VIEW_ITEM = 1;
    protected static final int TYPE_VIEW_LOADING = 2;

    private int mVisibleThreshold = 5;
    private int mLastVisibleItem, mTotalItemCount;


    protected Context mContext;
    protected RecyclerView mRecyclerView;
    protected boolean mIsLoading;
    protected OnItemClickListener mClickListener;
    protected OnLoadMoreListener mOnLoadMoreListener;

    private List<T> mResources;

    protected AbstractSearchAdapter(@NonNull Context context, @NonNull RecyclerView recyclerView) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
        this.mResources = new ArrayList<>();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mResources.isEmpty())
                    return;

                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!mIsLoading && mTotalItemCount <= (mLastVisibleItem + mVisibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }

                    mIsLoading = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW_EMPTY)
            return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_empty, parent, false));

        if (viewType == TYPE_VIEW_LOADING)
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_loading, parent, false));

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AbstractSearchAdapter<?>.LoadingViewHolder)
            ((LoadingViewHolder)holder).onBindViewHolder(position);
    }

    @Override
    public int getItemCount() {
        return mResources != null && !mResources.isEmpty() ? mResources.size() : 1;
    }

    protected T getItem(int position) {
        return mResources.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mResources != null && !mResources.isEmpty() && mResources.get(position) == null
                ?
                TYPE_VIEW_LOADING : (mResources == null || mResources.isEmpty() ? TYPE_VIEW_EMPTY : TYPE_VIEW_ITEM);
    }

    public List<T> getResource() {
        return mResources;
    }

    public void setResource(List<T> resources) {
        mResources = resources;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setLoaded() {
        mIsLoading = false;
    }

    protected class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {
        protected ProgressBar mProgressBar;

        protected LoadingViewHolder(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar)itemView.findViewById(R.id.pb_recycler_loading_more);
        }

        void onBindViewHolder(int position) {
            mProgressBar.setIndeterminate(true);
        }
    }
}