package slidingmenu.android.library;

import com.nineoldandroids.view.ViewPropertyAnimator;

import android.view.View;

public class DefaultBehindAnimator implements SlidingMenu.Animatable {

    private float mSrcScale = 0.95f;

    private float mDstScale = 1.0f;

    private float mSrcAlpha = 0.6f;

    private float mDstAlpha = 1.0f;

    @Override
    public void animate(View view, float percent, boolean animate) {

        float scale = mSrcScale - (mSrcScale - mDstScale) * percent;

        float alpha = mSrcAlpha - (mSrcAlpha - mDstAlpha) * percent;

        if (view != null) {
            LogUtil.d("SlidingMenu", "alpha = " + alpha + "; scale = " + scale);
            ViewPropertyAnimator.animate(view).setDuration(animate ? 500 : 0).scaleX(scale).scaleY(scale).alpha(alpha)
                    .start();
        }
    }
}
