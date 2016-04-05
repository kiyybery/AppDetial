package com.xyn.appdetial.netapi.homeApi;


import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xyn.appdetial.Config;
import com.xyn.appdetial.netapi.ApiListener;
import com.xyn.appdetial.netapi.BaseApi;


/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class HomeApi extends BaseApi {

    private static final String TAG = HomeApi.class.getSimpleName();
    private static final String mBase_Url = Config.Consts.api_base + "socialportfolio";
    private static HomeApi mInstance;

    public static HomeApi getInstance() {
        if (mInstance == null) {

            mInstance = new HomeApi();
        }
        return mInstance;
    }

    public HttpHandler<String> getHomeList(int offset, int pageNum, ApiListener apiListener){

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("offset", offset + "");
        params.addQueryStringParameter("limit", pageNum + "");
        /*Log.d(TAG, "category_id=" + categoryId);
        Log.d(TAG, "order=" + trend);*/

        /*if (!TextUtils.isEmpty(categoryId) && !categoryId.equals("-1")) {
            params.addQueryStringParameter("category_id", categoryId);
        }
        if (!TextUtils.isEmpty(trend)) {
            params.addQueryStringParameter("order", trend);
        }*/
        return send(HttpRequest.HttpMethod.GET, mBase_Url, params, apiListener);
    }


}
