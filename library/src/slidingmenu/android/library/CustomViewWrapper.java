package slidingmenu.android.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

abstract class CustomViewWrapper extends ViewGroup {

    private View mContent = null;

    private float mOpenPercent = 0.0f;

    private SlidingMenu.Animatable mAnimator = null;

    CustomViewWrapper(Context context) {
        this(context, null);
    }

    CustomViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public final void setContent(View v) {
        if (mContent != null) {
            removeView(mContent);
        }

        mContent = v;
        addView(mContent);
    }

    public final View getContent() {
        return mContent;
    }

    public final void setAnimator(SlidingMenu.Animatable animator) {
        mAnimator = animator;
    }

    final void animate(float openPercent, boolean animate) {
        mOpenPercent = openPercent;

        if (mAnimator != null) {
            mAnimator.animate(mContent, openPercent, animate);
        }
    }

    final boolean isOpened() {
        return mOpenPercent == 1.0f;
    }

    final boolean isHalfOpened() {
        return mOpenPercent > 0.5f;
    }

    final void completeAnimate() {
        if (isHalfOpened()) {
            animate(1.0f, true);
        } else {
            animate(0.0f, true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);

        setMeasuredDimension(width, height);

        final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeight = getChildMeasureSpec(widthMeasureSpec, 0, height);

        if (mContent != null) {
            mContent.measure(contentWidth, contentHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;

        if (mContent != null) {
            mContent.layout(0, 0, width, height);
        }
    }

}
