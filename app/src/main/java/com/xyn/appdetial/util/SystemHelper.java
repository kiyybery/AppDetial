package com.xyn.appdetial.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.xyn.appdetial.App;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class SystemHelper {

    public static String getImei() {
        TelephonyManager tm = (TelephonyManager) App.get().getSystemService(
                Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "imei";
        }
        return TextUtils.isEmpty(tm.getDeviceId()) ? "imei" : tm.getDeviceId();
    }

    public static String getAndroidId() {
        String id = Settings.Secure.getString(App.get().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (TextUtils.isEmpty(id)) {
            return "androidId";
        }
        return id;
    }

    public static boolean isNetworkAvailable() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    public static boolean isWifiAvailable() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable()
                && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) App.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info;
    }

    public static int getVersionCode() {
        try {
            String pn = App.get().getPackageName();
            int vc = App.get().getPackageManager().getPackageInfo(pn, 0).versionCode;
            return vc;
        } catch (PackageManager.NameNotFoundException e) {
            return 1;
        }
    }

    public static String getVersoinName() {
        String pn = App.get().getPackageName();
        try {
            return App.get().getPackageManager().getPackageInfo(pn, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    public static String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) App.get().getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info == null) {
            return "macAddress";
        }
        if (TextUtils.isEmpty(info.getMacAddress())) {
            return "macAddress";
        }
        return info.getMacAddress();
    }

    public static String getDeviceType() {
        return TextUtils.isEmpty(Build.MODEL) ? "type"
                : Build.MODEL;
    }

    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }
}
