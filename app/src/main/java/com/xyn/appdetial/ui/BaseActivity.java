package com.xyn.appdetial.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.xyn.appdetial.R;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class BaseActivity extends AppCompatActivity{

    private static final int notifiId = 11;
    private static final String TAG = "BaseActivity";

//    protected LogCat L = LogCat.createInstance(this
//    );

    public TextView getToolbarTitle() {
        return toolbarTitle;
    }

    private TextView toolbarTitle;

    public Toolbar getToolbar() {
        return toolbar;
    }

    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.e(TAG, "警告onCreate(savedInstanceState, persistentState was called");
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolBar();

    }

    protected void initToolBar() {
//
        toolbar = (Toolbar) findViewById(R.id.my_toolbar_actionbar);

        if (toolbar == null) {
            Log.i(TAG, "initToolBar,however toolbar is null");
            return;
        }
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.arrow_left_drawable);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void setCenterTitle(String centerTitle) {
        if (toolbarTitle == null || TextUtils.isEmpty(centerTitle)) {
            return;
        }
        toolbarTitle.setText(centerTitle);
    }


    @Override
    public void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        Log.i(TAG, "onTitleChanged,toolBarTitle.setText()");
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // umeng

    }

//    @Override
//    public void onBackPressed() {
////       容易引起logic 无限的循环
////        FragmentManager fm = getFragmentManager();
////        if (fm.getBackStackEntryCount() > 0) {
////            fm.popBackStack();
////        } else {
////        }
//        super.onBackPressed();
//    }

    public void launch(Class<? extends BaseActivity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void launch(Class<? extends Fragment> fragment, Bundle argu4fragment) {
        launch(fragment, argu4fragment, 0);
    }

    protected void launch(Class<? extends Fragment> fragment, Bundle argu4fragment, int reqCode) {
        Intent in = ReusingActivityHelper.builder(this).setFragment(fragment, argu4fragment).build();
        if (reqCode != 0) {
            startActivityForResult(in, reqCode);
        } else {
            startActivity(in);
        }
    }

//
//    public void back(View view) {
//        finish();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onHandleBackAsUp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onHandleBackAsUp() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                    .startActivities();
        } else {
            // This activity is part of this app's task, so simply
            // navigate up to the logical parent activity.
            NavUtils.navigateUpTo(this, upIntent);
        }
    }
}
