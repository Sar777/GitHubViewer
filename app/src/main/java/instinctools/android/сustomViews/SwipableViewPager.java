package instinctools.android.сustomViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class SwipableViewPager extends ViewPager {

    public SwipableViewPager(Context context) {
        super(context);
    }

    public SwipableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof RecyclerView) {
            return dx <= 0 || (super.canScroll(v, checkV, dx, x, y));
        }

        return(super.canScroll(v, checkV, dx, x, y));
    }
}
