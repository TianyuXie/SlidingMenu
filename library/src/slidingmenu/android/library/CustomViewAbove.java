package slidingmenu.android.library;

import android.content.Context;
import android.util.AttributeSet;

class CustomViewAbove extends CustomViewWrapper {

    CustomViewAbove(Context context) {
        super(context);
    }

    CustomViewAbove(Context context, AttributeSet attrs) {
        super(context, attrs);

        setAnimator(new DefaultAboveAnimator());
    }
}
