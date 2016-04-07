package com.xyn.appdetial.ui.studyclass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xyn.appdetial.BaseListFragment;
import com.xyn.appdetial.R;
import com.xyn.appdetial.model.LibraryInfo;
import com.xyn.appdetial.netapi.ApiListener;
import com.xyn.appdetial.netapi.classApi.ClassApi;
import com.xyn.appdetial.ui.MutableAdapter;
import com.xyn.appdetial.util.GsonHelper;
import com.xyn.appdetial.util.ImageLoaderHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class ClassFragment extends BaseListFragment {

    public final static String TAG = ClassFragment.class.getSimpleName();

    private LayoutInflater mInflater;

    //热门默认为0、最新为1
    private int sortId = 0;
    //课程为0，路径图为1
    private int typeId = 0;

    public static ClassFragment newInstance() {

        ClassFragment cf = new ClassFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "课程");
        cf.setArguments(bundle);
        return cf;
    }

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
        adapter = new ClassAdapter();
        super.setupView(inflater, view, savedInstanceState);
    }

    @Override
    protected void loadData(int offset, int pageNum, ApiListener listener) {
        showLoading();
        new ClassApi().getClassesList(String.valueOf(""), offset, pageNum, String.valueOf(sortId), String.valueOf(typeId), listener);
    }

    @Override
    protected List parseData(String result) {
        if (!result.startsWith("[")) {

            return null;
        }

        return GsonHelper.parseList(result, new TypeToken<List<LibraryInfo>>() {
        }.getType());
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
        return R.layout.fragment_classlist;
    }

    private class ClassAdapter extends MutableAdapter<LibraryInfo> {

        @Override
        public LibraryInfo getItem(int position) {
            return data.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final LibraryInfo info = getItem(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.library_item, parent, false);
            }
            final EventViewHolder viewHolder = holdView(convertView);
            String path = info.getCover_path();
            ImageLoaderHelper.loadPic(ClassFragment.this, path, viewHolder.iv_library_cover, 0);

            if (info.getPayment_type().equals("1")) {
                viewHolder.linyout_library_guide.setVisibility(View.VISIBLE);
            } else {
                viewHolder.linyout_library_guide.setVisibility(View.INVISIBLE);
            }

            viewHolder.tv_library_title.setText(info.getTitle());
            viewHolder.tv_library_time.setText(changeTime(info.getDuration()));
            viewHolder.tv_library_count.setText(info.getStudent_count());
            viewHolder.tv_library_difficulty.setText(changeDifficulty(info.getDifficulty()));
            //设置收藏按钮形式
            if (info.getCollect().equals("1")) {
                viewHolder.ib_add_collections.setSelected(true);
            } else {
                viewHolder.ib_add_collections.setSelected(false);
            }
            viewHolder.iv_library_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            return convertView;
        }
    }

    private EventViewHolder holdView(View convertView) {
        EventViewHolder holder = (EventViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new EventViewHolder();
            convertView.setTag(holder);
        }
        holder.iv_library_cover = (ImageView) convertView.findViewById(R.id.iv_library_cover);
        holder.linyout_library_guide = (LinearLayout) convertView.findViewById(R.id.linyout_library_guide);
        holder.tv_library_title = (TextView) convertView.findViewById(R.id.tv_library_title);
        holder.tv_library_time = (TextView) convertView.findViewById(R.id.tv_library_time);
        holder.tv_library_count = (TextView) convertView.findViewById(R.id.tv_library_count);
        holder.tv_library_difficulty = (TextView) convertView.findViewById(R.id.tv_library_difficulty);
        holder.ib_add_collections = (ImageButton) convertView.findViewById(R.id.ib_add_collections);
        return holder;
    }

    static class EventViewHolder {
        private ImageView iv_library_cover;
        private LinearLayout linyout_library_guide;
        private TextView tv_library_title;
        private TextView tv_library_time;
        private TextView tv_library_count;
        private TextView tv_library_difficulty;
        private ImageButton ib_add_collections;
    }

    //时间转换的方法
    private String changeTime(int time) {
        String newTime = "";

        if (time > 3600) {
            int hour = time / 3600;
            int min = (time - hour * 3600) / 60;
            newTime = hour + "h" + min + "m";
        } else {
            int min = time / 60;
            newTime = min + "m";
        }

        return newTime;
    }

    //难度转换的方法
    private String changeDifficulty(String difficulty) {
        String newDefficulty = "";
        if (difficulty.equals("1")) {
            newDefficulty = "入门级";
        } else {
            newDefficulty = "进阶级";
        }
        return newDefficulty;
    }
}
