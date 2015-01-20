package slidingmenu.android.library;

import com.nineoldandroids.view.ViewPropertyAnimator;

import android.view.View;

class DefaultAboveAnimator implements SlidingMenu.Animatable {

    private int mSrcTranslationX = 0;

    private int mDstTranslationX = 300;

    private float mSrcScale = 1.0f;

    private float mDstScale = 0.9f;

    @Override
    public void animate(View view, float percent, boolean animate) {

        float translationX = mSrcTranslationX - (mSrcTranslationX - mDstTranslationX) * percent;

        float scale = mSrcScale - (mSrcScale - mDstScale) * percent;

        if (view != null) {
            LogUtil.d("SlidingMenu", "translationX = " + translationX + "; scale = " + scale);
            ViewPropertyAnimator.animate(view).setDuration(animate ? 500 : 0).scaleX(scale).scaleY(scale)
                    .translationX(translationX).start();
        }
    }
}
