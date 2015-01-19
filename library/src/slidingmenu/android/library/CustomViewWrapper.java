package slidingmenu.android.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

class CustomViewWrapper extends ViewGroup {

    private View mContent;

    public CustomViewWrapper(Context context) {
        this(context, null);
    }

    public CustomViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setContent(View v) {
        if (mContent != null) {
            removeView(mContent);
        }

        mContent = v;
        addView(mContent);
    }

    public View getContent() {
        return mContent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);

        setMeasuredDimension(width, height);

        final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeight = getChildMeasureSpec(widthMeasureSpec, 0, height);

        mContent.measure(contentWidth, contentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;

        mContent.layout(0, 0, width, height);
    }
}