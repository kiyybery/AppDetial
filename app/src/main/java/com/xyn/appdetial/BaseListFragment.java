package com.xyn.appdetial;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xyn.appdetial.netapi.ApiListener;
import com.xyn.appdetial.netapi.BaseApiListener;
import com.xyn.appdetial.ui.MutableAdapter;
import com.xyn.appdetial.util.SystemHelper;
import com.xyn.appdetial.util.ToastHelper;
import com.xyn.appdetial.util.ViewHelper;
import com.xyn.appdetial.widget.PushListView;

import java.util.List;

/**
 * Created by Administrator on 2016/3/28 0028.
 */
public abstract class BaseListFragment<T> extends FragmentBase2 implements
        PushListView.OnRefreshListener, PushListView.OnLoadMoreListener {

    @ViewInject(R.id.list)
    protected PushListView listview;
    protected MutableAdapter<T> adapter;
    @ViewInject(R.id.loading)
    public View loading;
    @ViewInject(R.id.empty)
    protected View empty;

    @Override
    protected int getLayoutResId() {
        return R.layout.common_list;
    }

    @Override
    protected void setupView(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        super.setupView(inflater, view, savedInstanceState);

        listview = (PushListView) view.findViewById(R.id.list);
        loading = view.findViewById(R.id.loading);
        empty = view.findViewById(R.id.empty);
        listview.setOnRefreshListener(this);
        listview.setOnLoadMoreListener(this);

        adapter = createAdapter();

        listview.setAdapter(adapter);
        listview.setCanLoadMore(adapter.getCount() >= adapter.getNumPerPage());
        if (adapter.isEmpty()) {
            showLoading();
            if (!refreshDelay()) {
                onRefresh();
            }
        } else {
            showListView();
        }
    }

    protected boolean refreshDelay() {
        return false;
    }

    protected MutableAdapter createAdapter() {
        return new MutableAdapter();
    }

    protected abstract void loadData(int offset, int pageNum, ApiListener listener);

    protected abstract List<T> parseData(String result);

    protected void loadData(int offset, int numPerPage, final boolean refresh) {

        loadData(offset, numPerPage, new BaseApiListener(this) {

            @Override
            public void onOK(ResponseInfo<String> responseInfo) {
                super.onOK(responseInfo);
                List<T> data = parseData(responseInfo.result);

                if (refresh) {
                    adapter.updateData(data);
                    listview.onRefreshComplete();
                } else {
                    adapter.appendData(data);
                    listview.onLoadMoreComplete();
                }
                listview.setCanLoadMore(data != null
                        && data.size() >= adapter.getNumPerPage());
                if (adapter == null || adapter.getCount() == 0) {
                    if (getActivity() != null) {
                        showEmpty();
                    }
                } else {
                    showListView();
                }
            }

            @Override
            public void onNotOK(ResponseInfo<String> responseInfo) {
                super.onNotOK(responseInfo);
                if (refresh) {
                    listview.onRefreshComplete();
                } else {
                    listview.onLoadMoreComplete();
                }
                if (adapter == null || adapter.getCount() == 0) {
                    showLoadFailed();
                } else {
                    listview.setCanLoadMore(false);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                super.onFailure(error, msg);
                if (refresh) {
                    listview.onRefreshComplete();
                } else {
                    listview.onLoadMoreComplete();
                }
                if (adapter == null || adapter.getCount() == 0) {
                    showLoadFailed();
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        L.i("on load more");
        loadData(adapter.getCount(), adapter.getNumPerPage(), false);
    }

    @Override
    public void onRefresh() {
        L.i("on refresh");
        if (SystemHelper.isNetworkAvailable()) {
            loadData(0, adapter.getNumPerPage(), true);
        } else {
            ToastHelper.toast(this.getActivity(), "没有网络连接");
            showLoadFailed();
        }
    }

    protected void showLoading() {
        L.i("show loading");
        ViewHelper.showView(loading, false);
//        ViewHelper.goneView(listview, false);
        ViewHelper.goneView(empty, false);
    }

    protected void showListView() {
        L.i("show listview");
        ViewHelper.showView(listview, listview.getVisibility() == View.GONE);
        ViewHelper.goneView(loading, false);
        ViewHelper.goneView(empty, false);
    }

    protected void showEmpty() {
        if (getActivity() != null && isAdded()) {
            ViewHelper.showView(empty, false);
            ViewHelper.goneView(loading, false);
//            ViewHelper.goneView(listview, false);
            ImageView ei = (ImageView) empty.findViewById(R.id.empty_image);
            if (ei != null) {
                int i = (int) (Math.random() * 100000 % 20) + 1;
                int id = getResources().getIdentifier(
                        String.format("homepage_nodata%d", i), "drawable",
                        App.get().getPackageName());
                L.w("show empty", i, ":", id);
                ei.setImageResource(id);
            }
        }
    }

    protected void showLoadFailed() {
        showEmpty();
    }
}
