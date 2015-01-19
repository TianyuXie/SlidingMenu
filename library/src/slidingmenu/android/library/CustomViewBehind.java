package slidingmenu.android.library;

import com.nineoldandroids.view.ViewPropertyAnimator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class CustomViewBehind extends CustomViewWrapper {

    private float mSrcScale = 0.95f;

    private float mDstScale = 1.0f;

    private float mSrcAlpha = 0.6f;

    private float mDstAlpha = 1.0f;

    private float mOpenPercent = 0.0f;

    public CustomViewBehind(Context context) {
        super(context);
    }

    public CustomViewBehind(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void animate(float openPercent, boolean animate) {
        View content = getContent();

        float scale = mSrcScale - (mSrcScale - mDstScale) * openPercent;

        float alpha = mSrcAlpha - (mSrcAlpha - mDstAlpha) * openPercent;

        mOpenPercent = openPercent;

        if (content != null) {
            LogUtil.d("SlidingMenu", "alpha = " + alpha + "; scale = " + scale);
            ViewPropertyAnimator.animate(content).setDuration(animate ? 500 : 0).scaleX(scale).scaleY(scale)
                    .alpha(alpha).start();
        }
    }

    @Override
    boolean isOpened() {
        return mOpenPercent == 1.0f;
    }

    @Override
    boolean isHalfOpened() {
        return mOpenPercent > 0.5f;
    }
}
