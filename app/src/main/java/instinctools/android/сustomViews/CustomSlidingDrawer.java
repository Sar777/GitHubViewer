package instinctools.android.сustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

import instinctools.android.R;
import instinctools.android.animations.SlideAnimation;
import instinctools.android.сustomViews.listeners.CustomSlidingDrawerStateListener;

public class CustomSlidingDrawer extends FrameLayout implements Animation.AnimationListener {
    private static final String TAG = "CustomVerticalSlidingDrawer";

    public enum SlideState {
        Opened,
        Closed
    }

    // Listeners
    private CustomSlidingDrawerStateListener mListener;

    private final static int DEFAULT_ANIMATION_DURATION = 300;

    // Values
    private int mLayoutResourceId;
    private int mAnimationDuration;

    // View
    private ViewGroup mLayoutView;
    private int mDefaultViewHeight;

    private Button mButtonOpenClose;

    // Animations
    private Animation mOpenAnimation;
    private Animation mCloseAnimation;

    private Animation mButtonCloseAnimation;
    private Animation mButtonOpenAnimation;

    private SlideState mState;

    public CustomSlidingDrawer(Context context) {
        super(context);

        initView();

        initDefaultState();

        buildAnimations();
    }

    public CustomSlidingDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);

        initView();

        initDefaultState();

        buildAnimations();
    }

    public CustomSlidingDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);

        initView();

        initDefaultState();

        buildAnimations();
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomSlidingDrawer, 0, 0);
        try {
            mAnimationDuration = ta.getInt(R.styleable.CustomSlidingDrawer_animation_duration, DEFAULT_ANIMATION_DURATION);
            mLayoutResourceId = ta.getResourceId(R.styleable.CustomSlidingDrawer_layout, 0);
        } finally {
            ta.recycle();
        }

        if (mLayoutResourceId == 0)
            throw new IllegalArgumentException("CustomVerticalSlidingDrawer: Fail parse layout resource id.");
    }

    private void initDefaultState() {
        mState = SlideState.Closed;

        mLayoutView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mLayoutView.setVisibility(INVISIBLE);

        mDefaultViewHeight = mLayoutView.getMeasuredHeight();
    }

    private void buildAnimations() {
        // Open
        mOpenAnimation = new SlideAnimation(mLayoutView, 0, mDefaultViewHeight);
        mOpenAnimation.setInterpolator(new AccelerateInterpolator());
        mOpenAnimation.setDuration(mAnimationDuration);
        mOpenAnimation.setAnimationListener(this);

        // Close
        mCloseAnimation = new SlideAnimation(mLayoutView, mDefaultViewHeight, 0);
        mCloseAnimation.setInterpolator(new AccelerateInterpolator());
        mCloseAnimation.setDuration(mAnimationDuration);
        mCloseAnimation.setAnimationListener(this);

        mButtonCloseAnimation = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mButtonCloseAnimation.setInterpolator(new DecelerateInterpolator());
        mButtonCloseAnimation.setRepeatCount(0);
        mButtonCloseAnimation.setDuration(mAnimationDuration);
        mButtonCloseAnimation.setFillAfter(true);

        mButtonOpenAnimation = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mButtonOpenAnimation.setInterpolator(new DecelerateInterpolator());
        mButtonOpenAnimation.setRepeatCount(0);
        mButtonOpenAnimation.setDuration(mAnimationDuration);
        mButtonOpenAnimation.setFillAfter(true);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.layout_slider_header, this);

        mLayoutView = (ViewGroup) inflater.inflate(mLayoutResourceId, null);
        ViewGroup container = (ViewGroup) header.findViewById(R.id.sliding_frame_container);
        container.addView(mLayoutView);

        mButtonOpenClose = (Button)header.findViewById(R.id.button_open_filter);
        mButtonOpenClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutView.startAnimation(mState == SlideState.Closed ? mOpenAnimation : mCloseAnimation);
                mButtonOpenClose.startAnimation(mState == SlideState.Closed ? mButtonCloseAnimation : mButtonOpenAnimation);
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mState = mState == SlideState.Closed ? SlideState.Opened : SlideState.Closed;

        if (mListener != null) {
            if (mState == SlideState.Opened)
                mListener.onOpened();
            else
                mListener.onClosed();
        }

        if (mState == SlideState.Opened)
            mLayoutView.setVisibility(VISIBLE);
        else
            mLayoutView.setVisibility(INVISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void setOnStateListener(CustomSlidingDrawerStateListener listener) {
        this.mListener = listener;
    }

    public void measureViewLayout() {
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mLayoutView.measure(LayoutParams.MATCH_PARENT, height);

        if (mState == SlideState.Opened) {
            mLayoutView.setVisibility(INVISIBLE);
            mState = SlideState.Closed;

            Animation animation = new SlideAnimation(mLayoutView, mDefaultViewHeight, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(0);
            mLayoutView.startAnimation(animation);
        }

        mDefaultViewHeight = mLayoutView.getMeasuredHeight();
        buildAnimations();
    }
}
