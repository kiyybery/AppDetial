package com.xyn.appdetial.ui.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xyn.appdetial.BaseListFragment;
import com.xyn.appdetial.R;
import com.xyn.appdetial.model.TestItem;
import com.xyn.appdetial.netapi.ApiListener;
import com.xyn.appdetial.netapi.TestApi;
import com.xyn.appdetial.ui.MutableAdapter;
import com.xyn.appdetial.util.GsonHelper;
import com.xyn.appdetial.util.ImageLoaderHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class TestFragment extends BaseListFragment {

    public final static String TAG = TestFragment.class.getSimpleName();
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
        adapter = new TestAdapter();
        super.setupView(inflater, view, savedInstanceState);
    }

    @Override
    protected MutableAdapter createAdapter() {
        return adapter;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_testlist;
    }

    @Override
    protected void loadData(int offset, int pageNum, ApiListener listener) {

        showLoading();
    }

    @Override
    protected List parseData(String result) {
        if (!result.startsWith("[")) {

            return null;
        }
        return GsonHelper.parseList(result, new TypeToken<TestItem>() {
        }.getType());
    }

    private class TestAdapter extends MutableAdapter<TestItem> {

        @Override
        public TestItem getItem(int position) {
            return data.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final TestItem item = getItem(position);
            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.test_item, parent, false);
            }

            final EventViewHolder holder = holdView(convertView);
            String path = item.getImageUrl();
            ImageLoaderHelper.loadPic(TestFragment.this, path, holder.left_iv, 0);
            holder.title_tv.setText(item.getTitle());
            holder.time_tv.setText(item.getType());
            return convertView;
        }
    }

    private EventViewHolder holdView(View convertView) {
        EventViewHolder holder = (EventViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new EventViewHolder();
            convertView.setTag(holder);
        }

        holder.left_iv = (ImageView) convertView.findViewById(R.id.img_test);
        holder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
        holder.tag_iv = (ImageView) convertView.findViewById(R.id.tag_iv);
        holder.time_tv = (TextView) convertView.findViewById(R.id.answer_num);

        return holder;
    }

    static class EventViewHolder {

        public ImageView left_iv, tag_iv;
        public TextView title_tv, time_tv;
    }
}
