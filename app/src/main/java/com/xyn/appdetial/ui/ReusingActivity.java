package com.xyn.appdetial.ui;

import android.os.Bundle;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class ReusingActivity extends BaseActivity {

    private ReusingActivityHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ReusingActivityHelper.isSingleFragmentIntent(this)) {
            helper = new ReusingActivityHelper(this);
        }
        if (helper != null) {
            helper.requestWindowFeature();
            helper.setWindowFlags();
        }
        super.onCreate(savedInstanceState);
        if (helper != null) {
            helper.ensureFragment();
        }
    }
}
