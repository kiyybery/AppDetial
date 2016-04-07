package com.xyn.appdetial.netapi.classApi;

import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xyn.appdetial.Config;
import com.xyn.appdetial.netapi.ApiListener;
import com.xyn.appdetial.netapi.BaseApi;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class ClassApi extends BaseApi{

    private static final String TAG = ClassApi.class.getSimpleName();
    private static final String BaseUrl = Config.Consts.api_base + "course";

    private static ClassApi mInstance;

    public static ClassApi getInstance() {
        if (mInstance == null) {
            mInstance = new ClassApi();
        }
        return mInstance;
    }

    //获取课程列表的请求
    public HttpHandler<String> getClassesList(String topic, int offset, int limit, String sort, String type, ApiListener apiListener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("topic", topic);
        params.addQueryStringParameter("offset", offset + "");
        params.addQueryStringParameter("limit", limit + "");
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("type", type);

        return send(HttpRequest.HttpMethod.GET, BaseUrl, params, apiListener);
    }
}
