package com.xyn.appdetial.util;

import android.util.Log;


import com.xyn.appdetial.Config;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by lcx on 2014/12/31.
 */
public class LogCat {
    public static LogCat L = createInstance(LogCat.class);
    private String mTag;
    private boolean mDebug = Config.Consts.debug;

    private LogCat(Object obj) {
        mTag = getTagName(obj);
    }

    public static LogCat createInstance(Object obj) {
        return new LogCat(obj);
    }

    public void setDebug(boolean debug) {
        mDebug = debug;
    }

    public void setTag(Object o) {
        mTag = getTagName(o);
    }

    public void w(Object... m) {
        if (mDebug) {
            line();
            Log.w(mTag, join(m));
        }
    }

    public void d(Object... m) {
        if (mDebug) {
            line();
            Log.d(mTag, join(m));
        }
    }

    public void i(Object... m) {
        // if (mDebug) {
        // line();
        // Log.i(mTag, join(m));
        // }
        w(m);
    }

    public void e(String m, Throwable t) {
        if (t != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            // sLogger.setError(sw.toString());
        }
        if (mDebug) {
            line();
            Log.e(mTag, m, t);
        }
    }

    public void e(Throwable t) {
        e("", t);
    }

    public void e(Object... m) {
        if (mDebug) {
            line();
            Log.e(mTag, join(m));
            for (Object o : m) {
                if (o instanceof Exception) {
                    ((Exception) o).printStackTrace();
                }
            }
        }
    }

    private void line() {
        Log.w(mTag, "----------------------------------");
    }

    private String getTagName(Object o) {
        if (o instanceof Class<?>) {
            return ((Class<?>) o).getSimpleName();
        } else if (o instanceof CharSequence) {
            return (String) o;
        } else {
            return getTagName(o.getClass());
        }
    }

    private String join(Object[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Object o : arr) {
            if (o != null && o.getClass().isArray()) {
                Object[] a = (Object[]) o;
                sb.append("[");
                for (Object ao : a) {
                    sb.append(ao);
                    sb.append(",");
                }
                sb.append("]");
            } else {
                sb.append(String.valueOf(o));
            }
        }
        return sb.toString();
    }
}
