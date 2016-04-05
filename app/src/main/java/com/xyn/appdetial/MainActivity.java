package com.xyn.appdetial;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.xyn.appdetial.ui.home.HomeFragment;
import com.xyn.appdetial.ui.test.TestFragment;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private TestFragment testFragment;
    private Fragment[] mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
    }

    private void initFragment() {

        homeFragment = new HomeFragment();
        //testFragment = new TestFragment();
        mFragments = new Fragment[]{homeFragment};
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_frame, homeFragment);
        ft.show(homeFragment);
        ft.commit();
    }
}
