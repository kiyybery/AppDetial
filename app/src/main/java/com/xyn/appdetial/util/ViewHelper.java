package com.xyn.appdetial.util;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xyn.appdetial.App;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class ViewHelper {

    private static long ANIMATE_DURATION = 200;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static View setChildOnClickListener(View parent, int cid,
                                               View.OnClickListener listener) {
        if (parent != null) {
            View v = parent.findViewById(cid);
            if (v != null) {
                v.setOnClickListener(listener);
            }
            return v;
        }
        return null;
    }

    public static void setChildOnClickListener(View parent, int[] cids,
                                               View.OnClickListener listener) {
        if (parent != null && cids != null) {
            for (int cid : cids) {
                View v = parent.findViewById(cid);
                if (v != null) {
                    v.setOnClickListener(listener);
                }
            }
        }
    }

    public static TextView setTextView(View parent, int id, int resId) {
        return setTextView(parent, id, ResHelper.getString(resId));
    }

    public static TextView setTextView(View parent, int id, CharSequence string) {
        TextView t = (TextView) parent.findViewById(id);
        if (t != null) {
            t.setText(string);
        }
        return t;
    }

    public static String getTextViewText(View parent, int id) {
        TextView t = (TextView) parent.findViewById(id);
        if (t != null) {
            return t.getText().toString();
        } else {
            return null;
        }
    }

    public static boolean getCheckBoxValue(View parent, int id) {
        CheckBox cb = (CheckBox) parent.findViewById(id);
        return cb == null ? false : cb.isChecked();
    }

    public static void setCheckBoxValue(View parent, int id, boolean checked) {
        CheckBox cb = (CheckBox) parent.findViewById(id);
        cb.setChecked(checked);
    }

    public static void showChild(View parent, int child) {
        showChild(parent, parent.findViewById(child));
    }

    public static void showChild(View parent, View child) {
        if (!(parent instanceof ViewGroup)) {
            return;
        }
        ViewGroup g = (ViewGroup) parent;
        for (int i = 0; i < g.getChildCount(); ++i) {
            View v = g.getChildAt(i);
            if (v == child) {
                showView(v, false);
                v.bringToFront();
            } else {
                goneView(v, false);
            }
        }
    }

    public static View showView(View v, boolean animate, long duration) {
        if (v != null) {
            v.setVisibility(View.VISIBLE);
            if (animate) {
                Animation ani = new AlphaAnimation(0f, 1f);
                ani.setDuration(duration);
                v.startAnimation(ani);
            }
        }
        return v;
    }

    public static View showView(View v, boolean animate) {
        return showView(v, animate, ANIMATE_DURATION);
    }

    public static View goneView(View v, boolean animate) {
        if (v != null) {
            v.setVisibility(View.GONE);
            if (animate) {
                Animation ani = new AlphaAnimation(1f, 0f);
                ani.setDuration(ANIMATE_DURATION);
                v.startAnimation(ani);
            }
        }
        return v;
    } public static void goneView(ViewGroup parent,int resId) {
        View v = parent.findViewById(resId);
        if (v != null) {
            v.setVisibility(View.GONE);
        }
    }

    public static View invisibleView(View v, boolean animate) {
        if (v != null) {
            v.setVisibility(View.INVISIBLE);
            if (animate) {
                Animation ani = new AlphaAnimation(1f, 0f);
                ani.setDuration(ANIMATE_DURATION);
                v.startAnimation(ani);
            }
        }
        return v;
    }

    public static void hideKeyboard(View et) {
        if (et == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) App.get()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showKeyboard(View et) {
        InputMethodManager imm = (InputMethodManager) App.get()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
    }

    public static void setBackgroundColor(View v, int resId) {
        if (v != null) {
            v.setBackgroundColor(ResHelper.getColor(resId));
        }
    }

    public static void setBackground(View v, int resId) {
        if (v != null) {
            v.setBackgroundResource(resId);
        }
    }

    public static void setImageViewSrc(View parent, int id, int resId) {
        ImageView v = (ImageView) parent.findViewById(id);
        if (v != null) {
            v.setImageDrawable(ResHelper.getDrawable(resId));
        }
    }

    public static void setDicemalEditFilter(EditText inputEdit, final int length) {
        InputFilter lengthfilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if ("".equals(source.toString())) {
                    return null;
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    int diff = dotValue.length() + 1 - length;
                    if (diff > 0 && start >= 0 && start < source.length()
                            && end - diff >= 0 && end - diff < source.length()
                            && start <= end - diff) {
                        return source.subSequence(start, end - diff);
                    } else {
                        return null;
                    }
                }
                return null;
            }
        };
        inputEdit.setFilters(new InputFilter[]{lengthfilter});
    }
}
