package com.xyn.appdetial.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;


public class ToastHelper {

    private static final int length = 24;

    private static final int error = 0;

    private static final int info = 1;

    public static void toast(Context context, String text) {
        innerToast(context, text, info);
    }

    public static void toast(Context context, int resId) {
        toast(context, ResHelper.getString(resId));
    }

    public static void toast(Fragment fragment, String text) {
        if (fragment != null && fragment.getActivity() != null) {
            toast(fragment.getActivity(), text);
        }
    }

    public static void toast(Fragment fragment, int resId) {
        toast(fragment, ResHelper.getString(resId));
    }

    public static void error(Fragment fragment, String text) {
        innerToast(fragment, text, error);
    }

    public static void error(Fragment fragment, int resId) {
        error(fragment, ResHelper.getString(resId));
    }

    public static void error(Context context, String text) {
        innerToast(context, text, error);
    }

    public static void error(Context context, int resId) {
        error(context, ResHelper.getString(resId));
    }

    private static void innerToast(Fragment fragment, String text, int type) {
        if (fragment != null) {
            innerToast(fragment.getActivity(), text, type);
        }
    }

    private static void innerToast(Context context, String text, int type) {
        if (TextUtils.isEmpty(text) & context == null) {
            return;
        }
        try {
            // why npe ?
            Toast toast = Toast.makeText(context, text,
                    text.length() > length ? Toast.LENGTH_LONG
                            : Toast.LENGTH_SHORT);
            // switch (type) {
            // case info:
            // toast.getView().setBackgroundColor(
            // ResHelper.getColor(R.color.main_color));
            // break;
            // case error:
            // toast.getView().setBackgroundColor(
            // ResHelper.getColor(R.color.danger));
            // break;
            // }
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {

        }
    }
}
