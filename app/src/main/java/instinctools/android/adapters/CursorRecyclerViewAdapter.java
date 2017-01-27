package instinctools.android.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

public abstract class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    static final int VIEW_TYPE_ITEM = 1;
    static final int VIEW_TYPE_HEADER = 2;
    static final int VIEW_TYPE_EMPTY = 3;

    private Context mContext;

    protected Cursor mCursor;

    protected boolean mDataValid;

    private int mRowIdColumn;
    private String mRowIdColumnName;
    private final boolean mCanShowHeader;

    private DataSetObserver mDataSetObserver;

    CursorRecyclerViewAdapter(String idColumnName, Context context, boolean showHeader, @Nullable Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mCanShowHeader = showHeader;
        mDataValid = cursor != null;
        mRowIdColumnName = idColumnName;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex(mRowIdColumnName) : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null)
            mCursor.registerDataSetObserver(mDataSetObserver);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCanShowHeader ? mCursor.getCount() + 1 : (mCursor.getCount() != 0 ? mCursor.getCount() : mCursor.getCount() + 1);
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(mCanShowHeader ? position - 1 : position))
            return mCursor.getLong(mRowIdColumn != -1 ? mRowIdColumn : mCursor.getColumnIndex(mRowIdColumnName));

        return 0;
    }

    public Cursor getCursor(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(mCanShowHeader ? position - 1 : position))
            return mCursor;

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataValid && mCursor != null) {
            if (position == 0)
                return mCursor.getCount() != 0 ? (mCanShowHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM): VIEW_TYPE_EMPTY;
        }

        return VIEW_TYPE_ITEM;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (!mDataValid)
            throw new IllegalStateException("this should only be called when the cursor is valid");

        if (!mCursor.moveToPosition(mCanShowHeader ? position - 1 : position))
            return;

        onBindViewHolder(viewHolder, mCursor);
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null && !old.isClosed()) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }
}