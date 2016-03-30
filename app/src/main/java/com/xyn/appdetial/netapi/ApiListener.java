package com.xyn.appdetial.netapi;

import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class ApiListener extends RequestCallBack<String> {


    private boolean isCancel = false;

    public boolean isCancel() {
        return isCancel;
    }

    public void cancel() {
        isCancel = true;
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo) {
        //        LogCat.L.e("Aplistener OnSuccess(result)", responseInfo.result);
        Log.i("ApiListener", "OnSuccess(状态码)" + responseInfo.statusCode);
//         todo  可以把statusCode==0 作为 cache 处理的依据
        int statusCode = responseInfo.statusCode;
        if (statusCode <= 302) {
            onOK(responseInfo);
        } else {
            onNotOK(responseInfo);
        }
        if (responseInfo.resultFormCache) {
            Log.w("ApiListener", "当前ApiListener result来自缓存");
        }
    }

    @Override
    public void onFailure(HttpException error, String msg) {
        Log.e("ApiListener", "--onFailure" + msg, error);
    }

    public void onOK(ResponseInfo<String> responseInfo) {
    }

    public void onNotOK(ResponseInfo<String> responseInfo) {
        Log.e("ApiLister" + "onNotOkReason", responseInfo.toString());
    }
}
