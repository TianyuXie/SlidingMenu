package slidingmenu.android.library;

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

    private CustomViewBehind mBehindView;

    private CustomViewAbove mAboveView;

    private VelocityTracker mVelocityTracker;

    private boolean mBeingDragged = false;

    private int mActivePointerId = INVALID_POINTER;

    private float mInitialMotionX = -1;

    private float mLastMotionX = -1;

    private float mLastMotionY = -1;

    private float mSrcTranslationX = 0f;

    private float mDstTranslationX = 200f;

    private float mOpenPercent = 0.0f;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0 /* defStyle */);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mBehindView = new CustomViewBehind(context, attrs);
        addView(mBehindView, 0);

        mAboveView = new CustomViewAbove(context, attrs);
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
        toggle(true);
    }

    public void toggle(boolean animate) {
        if (isOpened()) {
            showContent(animate);
        } else {
            showMenu(animate);
        }
    }

    public void showMenu() {
        showMenu(true);
    }

    public void showMenu(boolean animate) {
        mAboveView.animate(1.0f, true);
        mBehindView.animate(1.0f, true);
    }

    public void showContent() {
        showContent(true);
    }

    public void showContent(boolean animate) {
        mAboveView.animate(0.0f, true);
        mBehindView.animate(0.0f, true);
    }

    public boolean isOpened() {
        return mAboveView.isOpened() || mBehindView.isOpened();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;

        LogUtil.d("SlidingMenu", "onInterceptTouchEvent action: " + Util.convertMotionEvent2String(action));

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
            default:
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

        LogUtil.d("SlidingMenu", "onTouchEvent action: " + Util.convertMotionEvent2String(action));

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
                    final float y = MotionEventCompat.getY(ev, activePointerIndex);

                    final float deltaX = x - mLastMotionX;

                    mLastMotionX = x;
                    mLastMotionY = y;

                    float percent = mOpenPercent + deltaX / Math.abs(mSrcTranslationX - mDstTranslationX);

                    Log.d("SlidingMenu", "percent = " + percent + "; deltaX = " + deltaX);

                    if (deltaX > 0) {
                        percent = Math.min(1.0f, percent);
                    } else {
                        percent = Math.max(0f, percent);
                    }

                    mOpenPercent = percent;

                    Log.d("SlidingMenu", "percent = " + percent);

                    mAboveView.animate(percent, false);
                    mBehindView.animate(percent, false);

                }

                break;

            case MotionEvent.ACTION_UP:
                endDrag();
                break;
            default:
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
        if (!mBeingDragged) {
            mBeingDragged = true;
        }
    }

    private void endDrag() {
        if (mBeingDragged) {
            completeAnimate();
            mBeingDragged = false;
            mLastMotionX = -1;
            mLastMotionY = -1;
        }
    }

    private void completeAnimate() {
        mAboveView.completeAnimate();
        mBehindView.completeAnimate();
    }

    public interface OnOpenedListener {
        void onOpened();
    }

    public interface OnClosedListener {
        void onClosed();
    }

}
