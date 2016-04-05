package com.xyn.appdetial;

import android.app.Application;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class App extends Application{

    private static App APP;

    public static App get() {
        return APP;
    }

    @Override
    public void onCreate() {
        APP = this;
        super.onCreate();
    }
}
