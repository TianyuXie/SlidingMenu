package slidingmenu.android.library;

import com.nineoldandroids.view.ViewPropertyAnimator;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.FrameLayout;

public class SlidingMenu extends FrameLayout {

    public static final int LEFT = 0;

    public static final int RIGHT = 1;

    private static final int INVALID_POINTER = -1;

    private static final int INVALID_INDEX = -1;

    private int mMode = LEFT;

    private CustomViewWrapper mBehindView;

    private CustomViewWrapper mAboveView;

    private VelocityTracker mVelocityTracker;

    private boolean mBeingDragged = false;

    private int mActivePointerId = INVALID_POINTER;

    private float mInitialMotionX = -1;

    private float mLastMotionX = -1;

    private float mLastMotionY = -1;

    private int mCurTranslationX = 0;

    private float mCurScale = 1f;

    private int mDstTranslationX = 200;

    private float mDstScale = 0.9f;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0 /* defStyle */);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mBehindView = new CustomViewWrapper(context, attrs);
        addView(mBehindView, 0);

        mAboveView = new CustomViewWrapper(context, attrs);
        addView(mAboveView, 1);

    }

    public void setBehindView(int resId) {
        setBehindView(LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public void setBehindView(View view) {
        mBehindView.setContent(view);
    }

    public void setAboveView(int resId) {
        setAboveView(LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public void setAboveView(View view) {
        mAboveView.setContent(view);
    }

    public View getBehindView() {
        return mBehindView.getContent();
    }

    public View getAboveView() {
        return mAboveView.getContent();
    }

    public void toggle() {
        if (isMenuShowing()) {
            showContent();
        } else {
            showMenu();
        }
    }

    public void showMenu() {
        animate(mDstTranslationX, mDstScale, true /* animate */);
    }

    public void showContent() {
        animate(0f /* translationX */, 1.0f /* scale */, true /* animate */);
    }

    public void animate(float translationX, float scale, boolean animate) {
        if (mAboveView != null) {
            Log.d("SlidingMenu", "translationX = " + translationX + "; scale = " + scale);
            mCurTranslationX = (int) translationX;
            mCurScale = scale;
            ViewPropertyAnimator.animate(mAboveView).setDuration(animate ? 500 : 0).scaleX(mCurScale).scaleY(mCurScale)
                    .translationX(mCurTranslationX).start();
        }
    }

    public boolean isMenuShowing() {
        return mCurTranslationX == mDstTranslationX;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;

        Log.w("SlidingMenu", "onInterceptTouchEvent action: " + Util.convertMotionEvent2String(action));

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);

                if (mActivePointerId == INVALID_POINTER) {
                    break;
                }

                mLastMotionX = mInitialMotionX = MotionEventCompat.getX(ev, index);
                mLastMotionY = MotionEventCompat.getY(ev, index);
                break;
            case MotionEvent.ACTION_MOVE:
                determineDrag(ev);
                break;
        }

        if (!mBeingDragged) {
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            }

            mVelocityTracker.addMovement(ev);
        }

        return mBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;

        Log.w("SlidingMenu", "onTouchEvent action: " + Util.convertMotionEvent2String(action));
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:

                if (!mBeingDragged) {
                    determineDrag(ev);
                }

                if (mBeingDragged) {
                    final int activePointerIndex = getPointerIndex(ev, mActivePointerId);
                    if (mActivePointerId == INVALID_POINTER) {
                        break;
                    }

                    final float x = MotionEventCompat.getX(ev, activePointerIndex);
                    final float deltaX = x - mLastMotionX;

                    mLastMotionX = x;

                    float translationX = mCurTranslationX + deltaX;
                    if (deltaX > 0) {
                        translationX = (int) Math.min(mDstTranslationX, translationX);
                    } else {
                        translationX = (int) Math.max(0f, translationX);
                    }

                    float scale = 1.0f - (1.0f - mDstScale) * (translationX / mDstTranslationX);

                    Log.d("SlidingMenu", "scale = " + scale);

                    if (deltaX > 0) {
                        scale = Math.max(mDstScale, scale);
                    } else {
                        scale = Math.min(1.0f, scale);
                    }

                    animate(translationX, scale, false);
                }

                break;

            case MotionEvent.ACTION_UP:
                completeAnimate();
                endDrag();
                break;
        }

        return true;
    }

    private int getPointerIndex(MotionEvent ev, int id) {
        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, id);
        if (activePointerIndex == INVALID_INDEX) {
            mActivePointerId = INVALID_POINTER;
        }

        return activePointerIndex;
    }

    private void determineDrag(MotionEvent ev) {
        final int activePorinterId = mActivePointerId;
        final int pointerIndex = getPointerIndex(ev, activePorinterId);

        if (activePorinterId == INVALID_POINTER || pointerIndex == INVALID_INDEX) {
            return;
        }

        final float x = MotionEventCompat.getX(ev, pointerIndex);
        final float deltaX = x - mLastMotionX;
        final float diffX = Math.abs(deltaX);

        final float y = MotionEventCompat.getY(ev, pointerIndex);
        final float deltaY = y - mLastMotionY;
        final float diffY = Math.abs(deltaY);

        if (diffX > diffY) {
            startDrag();
            mLastMotionX = x;
            mLastMotionY = y;
        }
    }

    private void startDrag() {
        mBeingDragged = true;
    }

    private void endDrag() {
        mBeingDragged = false;
    }

    private void completeAnimate() {
        if (mCurTranslationX > mDstTranslationX / 2) {
            showMenu();
        } else {
            showContent();
        }
    }

    public interface OnOpenedListener {
        void onOpened();
    }

    public interface OnClosedListener {
        void onClosed();
    }

}
