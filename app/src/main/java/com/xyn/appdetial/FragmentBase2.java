package com.xyn.appdetial;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.xyn.appdetial.ui.ReusingActivityHelper;
import com.xyn.appdetial.util.DialogHelper;
import com.xyn.appdetial.util.LogCat;
import com.xyn.appdetial.util.ViewHelper;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public abstract class FragmentBase2 extends FragmentBase{

    private static final String TAG = FragmentBase.class.getSimpleName();
    protected LogCat L = LogCat.createInstance(this);

    protected View parent;

    @Deprecated
    private ProgressDialog progressDialog;


    public FragmentBase2() {

    }

    public Toolbar getToolbar() {
        return toolbar;
    }


    /**
     * 有toolbar 的基本上就是需要有arrow 的
     */
    @Override
    protected void initToolBar(View rootView) {
        toolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar_actionbar);
        if (toolbar != null) {
            toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbar.setNavigationIcon(R.drawable.arrow_left_drawable);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    getActivity().onBackPressed();
                }
            });
        }

    }

    protected void onHandleBackAsUp() {
        Intent upIntent = NavUtils.getParentActivityIntent(mActivity);
        if (NavUtils.shouldUpRecreateTask(mActivity, upIntent)) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(mActivity)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                    .startActivities();
        } else {
            // This activity is part of this app's task, so simply
            // navigate up to the logical parent activity.
            NavUtils.navigateUpTo(mActivity, upIntent);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public String getCurrentFragName() {
        return CurrentFragName;
    }

    public void setCurrentFragName(String currentFragName) {
        CurrentFragName = currentFragName;
    }

    private String CurrentFragName;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        parent = inflater.inflate(getLayoutResId(), container, false);
        ViewUtils.inject(this, parent);
        setupView(inflater, parent, savedInstanceState);
        return parent;
    }

    protected String getTextString(int id) {
        return ViewHelper.getTextViewText(parent, id);
    }

    protected View getView(int id) {
        return parent.findViewById(id);
    }

    protected void setTextString(int id, String string) {
        ViewHelper.setTextView(parent, id, string);
    }

    protected void showView(int resId) {
        ViewHelper.showChild(parent, resId);
    }

    protected void goneView(int resId) {
        ViewHelper.goneView((ViewGroup) parent, resId);
    }

    public void setImageSrc(int id, int ResId) {
        ViewHelper.setImageViewSrc(parent, id, ResId);
    }

    protected abstract int getLayoutResId();

    protected void setupView(LayoutInflater inflater, View view,
                             Bundle savedInstanceState) {
    }

    protected void launch(Class<? extends Fragment> fragment,
                          Bundle args4fragment) {
        launch(fragment, args4fragment, 0);
    }

    protected void launch(Class<? extends Fragment> fragment,
                          Bundle args4fragment, int reqCode) {
        Intent in = ReusingActivityHelper.builder(this)
                .setFragment(fragment, args4fragment).build();
        if (reqCode != 0) {
            startActivityForResult(in, reqCode);
        } else {
            startActivity(in);
        }
    }


    protected void setTitle(String title) {
        if (toolbarTitle != null && title != null) {
            toolbarTitle.setText(title);
        }
    }

    protected void setTitle(int resId) {
        setTitle(getString(resId));
    }

    protected void finish() {
        finish(Activity.RESULT_CANCELED, null);
    }

    protected void finish(int resultCode, Intent data) {
        if (getActivity() != null) {
            getActivity().setResult(resultCode, data);
            getActivity().finish();
        }
    }

    protected void showProgress(String title, int resId) {
        showProgress(title, getString(resId), null);
    }

    protected void showProgress(String title, String message) {
        showProgress(title, message, null);
    }

    protected void showProgress(String title, int resId,
                                DialogInterface.OnCancelListener listener) {
        showProgress(title, getString(resId), listener);
    }

    protected void showProgress(String title, String message,
                                DialogInterface.OnCancelListener listener) {
        progressDialog = DialogHelper.showProgressDialog(getActivity(), title,
                message);
        if (listener != null) {
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(listener);
        }
    }

    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.hide();
        }
    }
    @Deprecated
    protected AlertDialog showConfirmDialog(CharSequence title,
                                            CharSequence message, DialogHelper.ConfirmCallback callback) {
        return DialogHelper
                .showConfirm(getActivity(), title, message, callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }
}
