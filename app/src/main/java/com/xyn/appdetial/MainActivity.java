package com.xyn.appdetial;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.xyn.appdetial.ui.BaseActivity;
import com.xyn.appdetial.ui.home.HomeFragment;
import com.xyn.appdetial.ui.profire.MyProfireFragment;
import com.xyn.appdetial.ui.release.ReleaseFragment;
import com.xyn.appdetial.ui.studyclass.ClassFragment;

public class MainActivity extends BaseActivity {

    private HomeFragment homeFragment;
    private ClassFragment classFragment;
    private ReleaseFragment releaseFragment;
    private MyProfireFragment profireFragment;
    private Fragment[] mFragments;
    private int index;
    private ImageButton ib_camera;
    private int currentTabIndex;
    private FrameLayout[] mTabs;

    //    tabIndicator
    private View tabIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
        initTabs();

        tabIndicator = findViewById(R.id.mainTabIndicator);
    }

    private void initFragment() {

        homeFragment = HomeFragment.newInstance();
        classFragment = ClassFragment.newInstance();
        releaseFragment = new ReleaseFragment();
        profireFragment = new MyProfireFragment();

        mFragments = new Fragment[]
                {homeFragment, classFragment, releaseFragment, profireFragment};
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_frame, homeFragment);
        ft.add(R.id.main_frame, classFragment);
        ft.add(R.id.main_frame, releaseFragment);
        ft.add(R.id.main_frame, profireFragment);

        ft.hide(classFragment).hide(releaseFragment).hide(profireFragment).show(homeFragment);
        ft.commit();

        currentTabIndex = 0;
    }

    private void initTabs() {

        mTabs = new FrameLayout[3];

        mTabs[0] = (FrameLayout) findViewById(R.id.maintab1);
        mTabs[1] = (FrameLayout) findViewById(R.id.maintab2);
        mTabs[2] = (FrameLayout) findViewById(R.id.maintab3);

        ib_camera = (ImageButton) findViewById(R.id.ib_generate_works);
    }

    private float offsetHome, offsetDiscover, offsetMsg, offsetProfile;

    private void initIndicatorOffset() {
        offsetHome = findViewById(R.id.maintab1).getX();
        offsetDiscover = findViewById(R.id.maintab2).getX();
        offsetProfile = findViewById(R.id.maintab3).getX();
    }

    public void onClickTab(View view) {

        if (view.getId() == R.id.ib_generate_works) {

            //go to publishWorkActivity
        } else {

            initIndicatorOffset();
            switch (view.getId()) {

                case R.id.maintab1:
                    index = 0;
                    tabIndicator.animate().x(offsetHome);
                    break;

                case R.id.maintab2:
                    index = 1;
                    tabIndicator.animate().x(offsetDiscover);
                    break;

                case R.id.maintab3:
                    index = 2;
                    tabIndicator.animate().x(offsetProfile);
                    break;
            }
            if (currentTabIndex != index) {

                android.support.v4.app.FragmentTransaction ft =
                        getSupportFragmentManager().beginTransaction();
                ft.hide(mFragments[currentTabIndex]);
                if (!mFragments[index].isAdded()) {

                    ft.add(R.id.main_frame, mFragments[index]);
                }
                ft.show(mFragments[index]).commit();
            }

            mTabs[currentTabIndex].setSelected(false);
            mTabs[index].setSelected(true);
            currentTabIndex = index;
        }
    }
}
