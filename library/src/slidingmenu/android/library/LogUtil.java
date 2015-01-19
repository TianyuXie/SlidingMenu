package slidingmenu.android.library;

import android.util.Log;

public class LogUtil {

    private static int LOG_LEVEL = Log.VERBOSE;

    public static void setLevel(int level) {
        LOG_LEVEL = level;
    }

    public static void v(String tag, String msg) {
        println(Log.VERBOSE, tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        println(Log.VERBOSE, tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        println(Log.DEBUG, tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        println(Log.DEBUG, tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        println(Log.INFO, tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        println(Log.INFO, tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        println(Log.WARN, tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        println(Log.WARN, tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        println(Log.ERROR, tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        println(Log.ERROR, tag, msg, tr);
    }

    public static void println(int priority, String tag, String msg, Throwable tr) {
        println(priority, tag, msg + "\n" + android.util.Log.getStackTraceString(tr));
    }

    public static void println(int priority, String tag, String msg) {
        if (priority >= LOG_LEVEL) {
            Log.println(priority, tag, msg);
        }
    }
}
