package com.xyn.appdetial.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.xyn.appdetial.R;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class DialogHelper {

    public static ProgressDialog showProgressDialog(Context context,
                                                    String title, String message) {
        return ProgressDialog.show(context, title, message);
    }

    public static AlertDialog showSingleConfirm(Context context, CharSequence title, CharSequence message, final ConfirmCallback callback) {
        if (context == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogInterface.OnClickListener ln = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    callback.onPositive(dialog);
                }
            }
        };
        return builder.setTitle(title).setMessage(message)
                .setPositiveButton(callback.getPositiveHint(), ln).show();
    }

    public static AlertDialog showConfirm(Context context, CharSequence title,
                                          CharSequence message, final ConfirmCallback callback) {
        if (context == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogInterface.OnClickListener ln = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    callback.onPositive(dialog);
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    callback.onNegative(dialog);
                }
            }
        };
        return builder.setTitle(title).setMessage(message)
                .setPositiveButton(callback.getPositiveHint(), ln)
                .setNegativeButton(callback.getNegativeHint(), ln).show();
    }

    public static AlertDialog showSingleChoiceDialog(Context context,
                                                     String title, String[] items, int checkedItem,
                                                     DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder.setTitle(title)
                .setSingleChoiceItems(items, checkedItem, listener).show();
    }

    public static AlertDialog showItemsDialog(Context context, String title,
                                              String[] items,int[] layout, final DialogInterface.OnClickListener listener) {
        return showItemsDialog(context, title, items, layout,listener, null);
    }

    public static AlertDialog showItemsDialog(Context context, String title,
                                              String[] items, int[] layout,final DialogInterface.OnClickListener listener,
                                              final ConfirmCallback callback) {
        DialogInterface.OnClickListener ln = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        callback.onPositive(dialog);
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        callback.onNegative(dialog);
                    }
                }
                if (listener != null) {
                    listener.onClick(dialog, which);
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (callback != null) {
            builder.setPositiveButton(callback.getPositiveHint(), ln)
                    .setNegativeButton(callback.getNegativeHint(), ln);
        }
        builder.setTitle(title).setItems(items, listener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        if (layout!=null&&layout.length>=2) {
            alertDialog.getWindow().setLayout(layout[0], layout[1]);
        }

        return alertDialog;
    }

    public interface ConfirmCallback {
        String getPositiveHint();

        void onPositive(DialogInterface dialog);

        String getNegativeHint();

        void onNegative(DialogInterface dialog);
    }

    public static class BaseConfirmCallback implements ConfirmCallback {

        @Override
        public String getPositiveHint() {
            return ResHelper.getString(R.string.ok);
        }

        @Override
        public void onPositive(DialogInterface dialog) {
            dialog.dismiss();
        }

        @Override
        public String getNegativeHint() {
            return ResHelper.getString(R.string.cancel);
        }

        @Override
        public void onNegative(DialogInterface dialog) {
            dialog.dismiss();
        }

    }
}
