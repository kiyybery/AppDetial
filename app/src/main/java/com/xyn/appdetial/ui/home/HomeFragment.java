package com.xyn.appdetial.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.xyn.appdetial.BaseListFragment;
import com.xyn.appdetial.R;
import com.xyn.appdetial.model.WorksItem;
import com.xyn.appdetial.model.WorksMember;
import com.xyn.appdetial.netapi.ApiListener;
import com.xyn.appdetial.netapi.homeApi.HomeApi;
import com.xyn.appdetial.ui.MutableAdapter;
import com.xyn.appdetial.util.GsonHelper;
import com.xyn.appdetial.util.ImageLoaderHelper;
import com.xyn.appdetial.widget.ImageViewWithGesture;

import java.util.List;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class HomeFragment extends BaseListFragment {

    public final static String TAG = HomeFragment.class.getSimpleName();

    private LayoutInflater mInflater;
    private int clickedPosition = -1;

    public static HomeFragment newInstance() {

        HomeFragment hf = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "首页");
        hf.setArguments(bundle);
        return hf;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setupView(LayoutInflater inflater, View view, Bundle savedInstanceState) {

        mInflater = inflater;
        adapter = new HomeAdapter();
        super.setupView(inflater, view, savedInstanceState);
    }

    @Override
    protected MutableAdapter createAdapter() {
        return adapter;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_homelist;
    }

    @Override
    protected void loadData(int offset, int pageNum, ApiListener listener) {
        showLoading();
        new HomeApi().getHomeList(offset, pageNum, listener);
    }

    @Override
    protected List parseData(String result) {
        if (!result.startsWith("[")) {
            return null;
        }
        return GsonHelper.parseList(result, new TypeToken<List<WorksItem>>() {
        }.getType());
    }

    private class HomeAdapter extends MutableAdapter<WorksItem> {

        @Override
        public WorksItem getItem(int position) {
            return data.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final WorksItem item = getItem(position);
            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.work_item, parent, false);
            }

            final EventViewHolder holder = holdView(convertView);
            String path = item.getPortfolio_detail();
            ImageLoaderHelper.loadPic(HomeFragment.this, path, holder.iv_works_cover, 0);
            holder.tv_works_title.setText(item.name);
            holder.tv_works_tag.setText(item.tag);
            holder.tv_works_view_count.setText(item.viewCount + "");
            holder.tv_works_like_count.setText(item.likeCount + "");
            holder.tv_works_comment_count.setText(item.commentCount + "");

            final WorksMember member = item.getMember();
            if (member == null) {
                return convertView;
            }
            //            头像事件
            final String avatar = member.getAvatar();
            Log.d(TAG, "avatar=" + avatar);
            ImageLoaderHelper.loadPic(HomeFragment.this, avatar, holder.iv_avatar, R.drawable.avatar_default);

            //          带视频的显示弹层
            boolean b = item.getVids().length > 0;
            holder.iv_interview_play.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent = new Intent();
                    Bundle data = new Bundle();
                    data.putString("uid", String.valueOf(member.getId()));
                    intent.putExtras(data);
                    intent.setClass(mActivity, OthersProfileActivity.class);
                    startActivityForResult(intent, 4);*/
                    clickedPosition = position;
                }
            });

            //            1.职业 2.签名3.位置
            String detail = member.getIndustry();
            if (TextUtils.isEmpty(detail)) {
                detail = member.getMyQuote();
            }
            if (!TextUtils.isEmpty(detail)) {
                holder.tv_works_author_detail.setVisibility(View.VISIBLE);
                holder.tv_works_author_detail.setText(detail);
            }

            //         双击点赞
            holder.tv_works_like_count.setSelected(!item.isLikeAble());


            return convertView;
        }
    }

    private EventViewHolder holdView(View convertView) {
        EventViewHolder holder = (EventViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new EventViewHolder();
            convertView.setTag(holder);
        }
        holder.iv_works_cover = (ImageViewWithGesture) convertView
                .findViewById(R.id.iv_works_cover);
        holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_works_avatar);
        holder.tv_works_title = (TextView) convertView.findViewById(R.id.tv_works_title);
        //        holder.tv_works_author_hint = (TextView) convertView.findViewById(R.id.tv_works_author_hint);
        holder.tv_works_author_name = (TextView) convertView.findViewById(R.id.tv_works_author_name);
        holder.tv_works_author_detail = (TextView) convertView.findViewById(R.id.tv_author_detail);

        holder.tv_works_tag = (TextView) convertView.findViewById(R.id.tv_works_works_tag);
        holder.tv_works_view_count = (TextView) convertView.findViewById(R.id.tv_works_view_count);
        holder.tv_works_like_count = (TextView) convertView.findViewById(R.id.tv_works_like_count);
        holder.tv_works_comment_count = (TextView) convertView.findViewById(R.id.tv_works_comment);
        holder.ibAddCollection = (ImageButton) convertView.findViewById(R.id.ib_add_collections);
        holder.ibAddConcern = (ImageButton) convertView.findViewById(R.id.ib_concern_from_workslist);
        holder.likeContainer = (FrameLayout) convertView.findViewById(R.id.LikeAnimatorContainer);
        holder.iv_interview_play = (ImageView) convertView.findViewById(R.id.iv_have_video);

        return holder;
    }

    static class EventViewHolder {
        public ImageViewWithGesture iv_works_cover;
        public ImageView iv_avatar;
        public TextView tv_works_title;
        //        public TextView tv_works_author_hint;
        public TextView tv_works_author_name;
        public TextView tv_works_author_detail;
        public TextView tv_works_tag;
        public TextView tv_works_view_count;
        public TextView tv_works_like_count;
        public TextView tv_works_comment_count;


        public ImageButton ibAddCollection;
        public ImageButton ibAddConcern;

        public FrameLayout likeContainer;
        public ImageView iv_interview_play;
    }
}
