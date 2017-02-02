package instinctools.android.adapters.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.listeners.OnItemClickListener;

public abstract class AbstractSearchAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_VIEW_EMPTY = 0;
    protected static final int TYPE_VIEW_ITEM = 1;

    protected Context mContext;
    protected OnItemClickListener mClickListener;

    private List<T> mResources;

    protected AbstractSearchAdapter(Context context) {
        this.mContext = context;
        this.mResources = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
        return mResources == null || mResources.isEmpty() ? TYPE_VIEW_EMPTY : TYPE_VIEW_ITEM;
    }

    public List<T> getResource() {
        return mResources;
    }

    public void setResource(List<T> resources) {
        mResources = resources;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    protected class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}