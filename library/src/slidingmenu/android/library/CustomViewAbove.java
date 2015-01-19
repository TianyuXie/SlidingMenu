package slidingmenu.android.library;

import com.nineoldandroids.view.ViewPropertyAnimator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class CustomViewAbove extends CustomViewWrapper {

    private int mSrcTranslationX = 0;

    private int mDstTranslationX = 300;

    private float mSrcScale = 1.0f;

    private float mDstScale = 0.9f;

    private float mOpenPercent = 0.0f;

    CustomViewAbove(Context context) {
        super(context);
    }

    CustomViewAbove(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void animate(float openPercent, boolean animate) {
        View content = getContent();

        float translationX = mSrcTranslationX - (mSrcTranslationX - mDstTranslationX) * openPercent;

        float scale = mSrcScale - (mSrcScale - mDstScale) * openPercent;

        mOpenPercent = openPercent;

        if (content != null) {
            LogUtil.d("SlidingMenu", "translationX = " + translationX + "; scale = " + scale);
            ViewPropertyAnimator.animate(content).setDuration(animate ? 500 : 0).scaleX(scale).scaleY(scale)
                    .translationX(translationX).start();
        }
    }

    boolean isOpened() {
        return mOpenPercent == 1.0f;
    }

    @Override
    boolean isHalfOpened() {
        return mOpenPercent > 0.5f;
    }
}
