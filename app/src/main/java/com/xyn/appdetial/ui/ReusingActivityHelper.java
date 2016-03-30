package com.xyn.appdetial.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class ReusingActivityHelper {

    public static final String SINGLE_FRAGMENT_ACTIVITY_START_ME_PARAM = "SINGLE_FRAGMENT_ACTIVITY_START_ME_PARAM";

    private static final String FRAGMENT_NAME = "fragment_name";

    private static final String FRAGMENT_TAG = "fragment_tag";

    private static final String FRAGMENT_ARGU = "fragment_argu";

    public static final String EXTRA = "extra";

    /**
     * 指定layout资源，为0或者不传，使用默认
     */
    public static final String EXTRA_PARAM_LAYOUT = "layout";

    /**
     * 指定fragment的container id,为0或者不传，使用默认
     */
    public static final String EXTRA_PARAM_CONTAINER = "container";

    /**
     * 指定activity 启动的window feature参数
     */
    public static final String EXTRA_PARAM_WINDOW_FEATURE = "window_feature";

    /**
     * 指定activity 启动的window frag参数,为一个数组，可以传递多个参数
     */
    public static final String EXTRA_PARAM_WINDOW_FLAGS = "window_flags";

    Fragment mFragment;

    ReusingActivity mActivity;

    ReusingActivityHelper(ReusingActivity activity) {
        mActivity = activity;
    }

    /**
     * 设置window feature参数
     *
     */
    void requestWindowFeature() {
        if (!isSingleFragmentIntent(mActivity)) {
            return;
        }
        Bundle param = mActivity.getIntent().getBundleExtra(
                SINGLE_FRAGMENT_ACTIVITY_START_ME_PARAM);
        Bundle extraParam = param.getBundle(EXTRA);
        if (extraParam != null
                && extraParam.containsKey(EXTRA_PARAM_WINDOW_FEATURE)) {
            int windowFeature = extraParam.getInt(EXTRA_PARAM_WINDOW_FEATURE,
                    Window.FEATURE_ACTION_BAR);
            mActivity.requestWindowFeature(windowFeature);
        }
    }

    /**
     * 设置window frag参数
     *
     */
    void setWindowFlags() {
        if (!isSingleFragmentIntent(mActivity)) {
            return;
        }

        Bundle param = mActivity.getIntent().getBundleExtra(
                SINGLE_FRAGMENT_ACTIVITY_START_ME_PARAM);
        Bundle extraParam = param.getBundle(EXTRA);
        if (extraParam.containsKey(EXTRA_PARAM_WINDOW_FLAGS)) {
            int flag = extraParam.getInt(EXTRA_PARAM_WINDOW_FLAGS);
            mActivity.getWindow().setFlags(flag, flag);
        }
    }

    /**
     * 初始化fragment
     */
    void ensureFragment() {
        Bundle param = mActivity.getIntent().getBundleExtra(
                SINGLE_FRAGMENT_ACTIVITY_START_ME_PARAM);
        if (param == null) {
            return;
        }
        ensureFragmentInternal(param);
    }

    /**
     * 初始化fragment
     *
     * @param param
     */
    private void ensureFragmentInternal(Bundle param) {
        Bundle extraParam = param.getBundle(EXTRA);

        // 设置activity layout
        int layoutid = extraParam == null ? 0 : extraParam.getInt(
                EXTRA_PARAM_LAYOUT, 0);
        if (layoutid != 0) {
            mActivity.setContentView(layoutid);
        }
        String fragmentTag = param.getString(FRAGMENT_TAG);
        mFragment = (Fragment) mActivity.getSupportFragmentManager()
                .findFragmentByTag(fragmentTag);
        if (mFragment == null) {
            // 初始化fragment
            String fragmentName = param.getString(FRAGMENT_NAME);
            Bundle argu = param.getBundle(FRAGMENT_ARGU);

            // 设置fragment container id
            int containerId = layoutid == 0 || extraParam == null ? 0
                    : extraParam.getInt(EXTRA_PARAM_CONTAINER, 0);
            if (containerId != 0) {
                mFragment = addFragmentByTag(containerId, fragmentName,
                        fragmentTag, argu);
            } else {
                mFragment = addFragmentByTag(android.R.id.content,
                        fragmentName, fragmentTag, argu);
            }
        }
    }

    /**
     * 增加fragment
     *
     * @param container
     * @param clazz
     * @param tag
     * @param argument
     * @return
     */
    private Fragment addFragmentByTag(int container, String clazz, String tag,
                                      Bundle argument) {
        FragmentManager fm = mActivity.getSupportFragmentManager();

        Fragment f = fm.findFragmentByTag(tag);
        if (f == null) {
            FragmentTransaction ft = fm.beginTransaction();
            f = Fragment.instantiate(mActivity, clazz, argument);
            if (container == 0) {
                ft.add(f, tag);
            } else {
                ft.add(container, f, tag);
            }

            ft.commit();
        } else if (f.isDetached()) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.attach(f);
            ft.commit();
        }
        return f;
    }

    static boolean isSingleFragmentIntent(BaseActivity activity) {
        Bundle param = activity.getIntent().getBundleExtra(
                SINGLE_FRAGMENT_ACTIVITY_START_ME_PARAM);
        return param != null;
    }

    public static IntentBuilder builder(Context context,
                                        Class<? extends BaseActivity> clazz) {
        IntentBuilder b = new IntentBuilder();
        b.create(context, clazz);
        return b;
    }

    public static IntentBuilder builder(Context context) {
        return builder(context, ReusingActivity.class);
    }

    public static IntentBuilder builder(Fragment fragment,
                                        Class<? extends BaseActivity> clazz) {
        return builder(fragment.getActivity(), clazz);
    }

    public static IntentBuilder builder(Fragment fragment) {
        return builder(fragment, ReusingActivity.class);
    }

    public static class IntentBuilder {
        private Intent intent;

        private Bundle args;

        private Bundle extra;

        public IntentBuilder create(Context context,
                                    Class<? extends Activity> clazz) {
            intent = new Intent(context, clazz);
            args = new Bundle();
            extra = new Bundle();
            return this;
        }

        public IntentBuilder setFragment(Class<? extends Fragment> fragment,
                                         Bundle args4fragment) {
            args.putString(FRAGMENT_NAME, fragment.getName());
//            xxx.class-->>xxx
            args.putString(FRAGMENT_TAG, fragment.getSimpleName());
            args.putBundle(FRAGMENT_ARGU, args4fragment);
            return this;
        }

        public IntentBuilder setFragment(String fragmentName,
                                         String fragmentTag, Bundle args4fragment) {
            args.putString(FRAGMENT_NAME, fragmentName);
            args.putString(FRAGMENT_TAG, fragmentTag);
            args.putBundle(FRAGMENT_ARGU, args4fragment);
            return this;
        }

        /**
         *
         */
        public IntentBuilder setExtra(Bundle extra) {
            this.extra.putAll(extra);
            return this;
        }

        /*
         * set layout of activity
         */
        public IntentBuilder setLayoutId(int layoutId) {
            extra.putInt(EXTRA_PARAM_LAYOUT, layoutId);
            return this;
        }

        /*
         * set container to put set fragment, default is android.R.id.content
         */
        public IntentBuilder setContainerId(int containerId) {
            extra.putInt(EXTRA_PARAM_CONTAINER, containerId);
            return this;
        }

        public IntentBuilder setWindowFlag(int windowFlag) {
            extra.putInt(EXTRA_PARAM_WINDOW_FLAGS, windowFlag);
            return this;
        }

        public IntentBuilder setWindowFeature(int windowFeature) {
            extra.putInt(EXTRA_PARAM_WINDOW_FEATURE, windowFeature);
            return this;
        }

        public Intent build() {
            args.putBundle(EXTRA, extra);
            intent.putExtra(SINGLE_FRAGMENT_ACTIVITY_START_ME_PARAM, args);
            return intent;
        }
    }
}
