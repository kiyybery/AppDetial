package com.xyn.appdetial.works;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xyn.appdetial.BaseListFragment;
import com.xyn.appdetial.R;
import com.xyn.appdetial.model.WorksItem;
import com.xyn.appdetial.netapi.ApiListener;
import com.xyn.appdetial.ui.MutableAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class WorksListFragment extends BaseListFragment {

    public final static String TAG = WorksListFragment.class.getSimpleName();
    private LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setupView(LayoutInflater inflater, View view, Bundle savedInstanceState) {

        mInflater = inflater;
        adapter = new WorksAdapter();
        super.setupView(inflater, view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected MutableAdapter createAdapter() {
        return adapter;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_works_list;
    }

    @Override
    protected void loadData(int offset, int pageNum, ApiListener listener) {

        showLoading();

    }

    @Override
    protected List parseData(String result) {
        return null;
    }

    private class WorksAdapter extends MutableAdapter<WorksItem> {


    }
}
