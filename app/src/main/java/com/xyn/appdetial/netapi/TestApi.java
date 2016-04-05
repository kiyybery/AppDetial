package com.xyn.appdetial.netapi;

import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xyn.appdetial.Config;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class TestApi extends BaseApi {

    private static final String TAG = TestApi.class.getSimpleName();
    private static final String mBase_Url = Config.Consts.api_base_test;
    private static TestApi mInstance;

    public static TestApi getInstance() {
        if (mInstance == null) {

            mInstance = new TestApi();
        }
        return mInstance;
    }

    public HttpHandler<String> getTestList(String category, int pageId, ApiListener apiListener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("category", category);
        params.addBodyParameter("pageId", pageId + "");

        return send(HttpRequest.HttpMethod.GET, mBase_Url, params, apiListener);
    }
}
