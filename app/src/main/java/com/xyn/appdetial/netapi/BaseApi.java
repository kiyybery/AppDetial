package com.xyn.appdetial.netapi;

import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xyn.appdetial.App;
import com.xyn.appdetial.util.LogCat;
import com.xyn.appdetial.util.NetUtils;
import com.xyn.appdetial.util.ToastHelper;

import org.apache.http.NameValuePair;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class BaseApi {

    protected LogCat L = LogCat.createInstance(this);

    protected HttpUtils httpUtils = new HttpUtils();

    protected HttpHandler<String> sendWithJsonParams(HttpRequest.HttpMethod method, String url,
                                                     RequestParams params, ApiListener listener) {

        params.addBodyParameter(ClientPNames.COOKIE_POLICY,
                CookiePolicy.BROWSER_COMPATIBILITY);

        StringBuilder sb = new StringBuilder("{");
        List<NameValuePair> bodyParams = params.getBodyParameters();
        if (bodyParams != null) {
            for (NameValuePair p : bodyParams) {
                sb.append("\"");
                sb.append(p.getName());
                String value = p.getValue();
                if (!TextUtils.isEmpty(value) && value.startsWith("{")
                        && value.endsWith("}")) {
                    // may be a json object, haha!
                    sb.append("\":");
                    sb.append(value);
                } else {
                    sb.append("\":\"");
                    sb.append(value);
                    sb.append("\"");
                }
                sb.append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("}");
        }

        try {
            params.setBodyEntity(new StringEntity(sb.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return send(method, url, params, listener);
    }

    protected HttpHandler<String> sendByPost(String url, RequestParams params,
                                             ApiListener listener) {
        return send(HttpRequest.HttpMethod.POST, url, params, listener);
    }

    protected HttpHandler<String> send(HttpRequest.HttpMethod method, final String url,
                                       RequestParams params, final ApiListener listener) {
        if (!NetUtils.hasNetwork(App.get())) {
            ToastHelper.toast(App.get(), "没有网络连接，请检查网络");
            return null;
        }

        if (!NetUtils.hasDataConnection(App.get())) {
            ToastHelper.toast(App.get(), "数据连接不可用，请检查网络设置");
            return null;
        }
//        Log.i("request请求url ", url);
//        httpUtils.configDefaultHttpCacheExpiry(1000);
        return httpUtils.send(method, url, params, listener);
    }

    protected HttpHandler<String> send(HttpRequest.HttpMethod method, final String url,
                                       RequestParams params, final ApiListener listener, boolean appId) {
        params.addBodyParameter("token", "");
//        if (appId) {
//            params.addBodyParameter("app_id", "1");
//        }
//        Log.w("request请求url及参数 ", url);
//        httpUtils.configDefaultHttpCacheExpiry(1000);
        return httpUtils.send(method, url, params, listener);

    }
}
