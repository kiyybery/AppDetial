package com.xyn.appdetial.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class MutableAdapter<T> extends BaseAdapter{

    public List<T> data;


    private int NumPerPage = 20;

    int selectpoistion = 0;
    public void setSelectpoistion(int position){

        selectpoistion = position;

    }
    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(List<T> data) {
        if (data == null) {
            return;
        }
        if (this.data == null) {
            this.data = new ArrayList<T>();
        } else {
            this.data.clear();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void appendData(List<T> data) {
        if (data == null) {
            return;
        }
        if (this.data == null) {
            this.data = new ArrayList<T>();
        }
        if (!this.data.isEmpty()) {
            this.data.removeAll(data);
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void appendData(T data) {
        if (data == null) {
            return;
        }
        if (this.data == null) {
            this.data = new ArrayList<T>();
        }
        this.data.add(data);
        notifyDataSetChanged();
    }

    public void removeData(Object data) {
        if (this.data != null) {
            this.data.remove(data);
            notifyDataSetChanged();
        }
    }
    // 貌似整数情况下会多一页？
    public int getPage() {
        if (this.data == null) {
            return 1;
        } else {
            return getCount() / getNumPerPage() + 1;
        }
    }
    //  获得每页的数据个数
    public int getNumPerPage() {
        return NumPerPage;
    }

    public void setNumPerPage(int numPerPage) {
        this.NumPerPage = numPerPage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t = new TextView(parent.getContext());
        t.setText(this.data.toString());
        return t;
    }
}
