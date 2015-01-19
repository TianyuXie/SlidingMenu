package slidingmenu.android.library;

import android.view.MotionEvent;

public abstract class Util {

    private Util() {
    }

    public static <T> String concat(T[] elements) {
        StringBuilder sb = new StringBuilder();
        boolean commanate = false;
        for (int i = 0; elements != null && i < elements.length; ++i) {
            if (commanate) {
                sb.append(",");
            }

            sb.append(elements[i].toString());
            commanate = true;
        }

        return sb.toString();
    }

    public static String convertMotionEvent2String(int action) {
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";

            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";

            case MotionEvent.ACTION_CANCEL:
                return "ACTION_CANCEL";

            case MotionEvent.ACTION_UP:
                return "ACTION_UP";
        }

        return "UNKNOWN";
    }
}
