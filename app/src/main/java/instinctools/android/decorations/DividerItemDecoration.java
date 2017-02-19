package instinctools.android.decorations;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by orion on 16.12.16.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private int mPaddingLeft;
    private Drawable mDivider;

    public DividerItemDecoration(int paddingLeft, Drawable drawable) {
        this.mPaddingLeft = paddingLeft;
        this.mDivider = drawable;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 1; i < childCount; ++i) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(mPaddingLeft, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
