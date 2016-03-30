package com.xyn.appdetial.netapi;

import android.app.Activity;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.xyn.appdetial.FragmentBase;
import com.xyn.appdetial.FragmentBase2;
import com.xyn.appdetial.ui.BaseActivity;
import com.xyn.appdetial.util.ToastHelper;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class BaseApiListener extends ApiListener {

    private BaseActivity activity;

    private FragmentBase2 fragment;

    public BaseApiListener() {
    }

    public BaseApiListener(BaseActivity activity) {
        this.activity = activity;
    }

    public BaseApiListener(FragmentBase2 fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onFailure(HttpException error, String msg) {
        super.onFailure(error, msg);

        Activity act = activity;
        if (act == null && fragment != null) {
            act = fragment.getActivity();
        }
        if (fragment != null) {
            fragment.dismissDialog();
        }
        if (act != null) {
            ToastHelper.toast(act, "哎呀，进不去了~看来网络有问题了");
        }
    }

    @Override
    public void onOK(ResponseInfo<String> responseInfo) {
        super.onOK(responseInfo);
        if (fragment != null) {
            fragment.dismissDialog();
        }
    }

    @Override
    public void onNotOK(ResponseInfo<String> responseInfo) {
        super.onNotOK(responseInfo);
        Activity act = activity;
        if (act == null && fragment != null) {
            act = fragment.getActivity();
        }
        if (fragment != null) {
            fragment.dismissDialog();
        }
    }

    // 传递fragment 和activity目的就是为了发送toast
    private Activity getActivity() {
        Activity act = activity;
        if (act == null && fragment != null) {
            act = fragment.getActivity();
        }
        return act;
    }
}
