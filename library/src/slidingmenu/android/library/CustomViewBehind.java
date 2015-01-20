package slidingmenu.android.library;

import android.content.Context;
import android.util.AttributeSet;

class CustomViewBehind extends CustomViewWrapper {

    CustomViewBehind(Context context) {
        super(context);
    }

    CustomViewBehind(Context context, AttributeSet attrs) {
        super(context, attrs);

        setAnimator(new DefaultBehindAnimator());
    }

}
