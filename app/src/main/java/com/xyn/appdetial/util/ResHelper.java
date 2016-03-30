package com.xyn.appdetial.util;

import android.graphics.drawable.Drawable;

import com.xyn.appdetial.App;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class ResHelper {
    public static String getString(int id) {
        return App.get().getResources().getString(id);
    }

    public static String getString(int id, Object... formatArgs) {
        return App.get().getString(id, formatArgs);
    }

    public static int getColor(int id) {
        return App.get().getResources().getColor(id);
    }

    public static Drawable getDrawable(int id) {
        return App.get().getResources().getDrawable(id);
    }
}
