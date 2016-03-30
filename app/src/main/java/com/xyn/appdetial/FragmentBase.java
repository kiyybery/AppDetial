package com.xyn.appdetial;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xyn.appdetial.ui.ProgressDialogBase;

/**
 * Created by Administrator on 2016/3/28 0028.
 */
public class FragmentBase extends Fragment {

    private static final String TAG = FragmentBase.class.getSimpleName();
    protected Toolbar toolbar;
    protected TextView toolbarTitle;

    /**
     * new Dialog util of mine
     */
    protected ProgressBar commonProgressBar;

    public void refreshFrag() {

    }

    protected boolean needRefresh;

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public ProgressDialogBase newProgressDiaFrag(String title, String msg) {
        progressDiagFrag = ProgressDialogBase.newInstance(title, msg);
        return progressDiagFrag;
    }

    public void dismissProgressDiaFrag() {
        if (progressDiagFrag != null) {
//            progressDiagFrag.getActivity().onBackPressed();
            progressDiagFrag.dismiss();
        }
    }

    protected ProgressDialogBase progressDiagFrag;

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AppCompatActivity) activity;
        Log.w(TAG, this.getClass().getSimpleName() + "--onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null; //非常有必要
        Log.w(TAG, this.getClass().getSimpleName() + "--onDetach");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolBar(view);
        initProgressbar(view);
        setTitle();
    }


    protected void setTitle() {
        if (getArguments() == null || toolbarTitle == null) {
            return;
        }
        String title = getArguments().getString("title");
        if (!TextUtils.isEmpty(title)) {
            toolbarTitle.setText(title);
        }
    }

    protected void initToolBar(View rootView) {
//
        toolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar_actionbar);

        if (toolbar == null) {
            Log.i(TAG, "initToolBar,however toolbar is null");
            return;
        }
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                getActivity().onBackPressed();
            }
        });
    }

    protected void initProgressbar(View view) {
        commonProgressBar = (ProgressBar) view.findViewById(R.id.commonProgressbar);
    }


}
