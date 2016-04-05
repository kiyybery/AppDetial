/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.http;

import android.os.SystemClock;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.DefaultHttpRedirectHandler;
import com.lidroid.xutils.http.callback.FileDownloadHandler;
import com.lidroid.xutils.http.callback.HttpRedirectHandler;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.callback.RequestCallBackHandler;
import com.lidroid.xutils.http.callback.StringDownloadHandler;
import com.lidroid.xutils.task.PriorityAsyncTask;
import com.lidroid.xutils.util.OtherUtils;
import com.xyn.appdetial.App;
import com.xyn.appdetial.netapi.PersistentCookieStore;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;


public class HttpHandler<T> extends PriorityAsyncTask<Object, Object, Void> implements RequestCallBackHandler {

    private final static int UPDATE_START = 1;
    private final static int UPDATE_LOADING = 2;
    private final static int UPDATE_FAILURE = 3;
    private final static int UPDATE_SUCCESS = 4;
    private static final NotUseApacheRedirectHandler notUseApacheRedirectHandler = new NotUseApacheRedirectHandler();
    private static final String TAG = "HttpHandler";
    private final AbstractHttpClient client;
    private final HttpContext context;
    private HttpRedirectHandler httpRedirectHandler;
    private String requestUrl;
    private String requestMethod;
    private HttpRequestBase request;
    private boolean isUploading = true;
    private RequestCallBack<T> callback;
    private int retriedCount = 0;
    private String fileSavePath = null;
    private boolean isDownloadingFile = false;
    private boolean autoResume = false; // Whether the downloading could continue from the point of interruption.
    private boolean autoRename = false; // Whether rename the file by response header info when the download completely.
    private String charset; // The default charset of response header info.
    private State state = State.WAITING;
    private long expiry = HttpCache.getDefaultExpiryTime();
    private long lastUpdateTime;

    // 执行请求

    public HttpHandler(AbstractHttpClient client, HttpContext context, String charset, RequestCallBack<T> callback) {
        this.client = client;
        this.context = context;
        this.callback = callback;
        this.charset = charset;
        this.client.setRedirectHandler(notUseApacheRedirectHandler);
//         设置Cookie 策略
        CookieSpecFactory csf = new CookieSpecFactory() {
            public CookieSpec newInstance(HttpParams params) {
                return new BrowserCompatSpec() {
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin)
                            throws MalformedCookieException {
                        // Oh, I am easy
                    }
                };
            }
        };

//        DefaultHttpClient httpclient = new DefaultHttpClient();
        this.client.getCookieSpecs().register("easy", csf);
        this.client.getParams().setParameter(
                ClientPNames.COOKIE_POLICY, "easy");
    }

    public void setHttpRedirectHandler(HttpRedirectHandler httpRedirectHandler) {
        if (httpRedirectHandler != null) {
            this.httpRedirectHandler = httpRedirectHandler;
        }
    }

    public State getState() {
        return state;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public RequestCallBack<T> getRequestCallBack() {
        return this.callback;
    }

    public void setRequestCallBack(RequestCallBack<T> callback) {
        this.callback = callback;
    }

    @SuppressWarnings("unchecked")
    private ResponseInfo<T> sendRequest(HttpRequestBase request) throws HttpException {

        HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
        while (true) {

            if (autoResume && isDownloadingFile) {
                File downloadFile = new File(fileSavePath);
                long fileLen = 0;
                if (downloadFile.isFile() && downloadFile.exists()) {
                    fileLen = downloadFile.length();
                }
                if (fileLen > 0) {
                    request.setHeader("RANGE", "bytes=" + fileLen + "-");
                }
            }

            boolean retry = true;
            IOException exception = null;
            try {
                requestMethod = request.getMethod();
                if (HttpUtils.sHttpCache.isEnabled(requestMethod)) {
                    String result = HttpUtils.sHttpCache.get(requestUrl);
                    if (result != null) {
                        return new ResponseInfo<T>(null, (T) result, true);
                    }
                }

                ResponseInfo<T> responseInfo = null;
                if (!isCancelled()) {
//                    发起requeset 之前带上cookie
                    PersistentCookieStore cookieStore = new PersistentCookieStore(App.get());
                    for (Cookie cookie : cookieStore.getCookies()) {
                        Log.w("本地获得==" + cookie.getName() + " " + cookie.getDomain(), cookie.getValue());
                    }
                    client.setCookieStore(cookieStore);
//                    request.setHeader();
                    HttpResponse response = client.execute(request, context);
                    List<Cookie> cookies = client.getCookieStore().getCookies();
                    for (Cookie cookie : cookies) {
                        cookieStore.addCookie(cookie);
                        Log.w("client.get==" + cookie.getName(), cookie.getValue());
                    }

                    responseInfo = handleResponse(response);
                }
                return responseInfo;
            } catch (UnknownHostException e) {
                exception = e;
                retry = retryHandler.retryRequest(exception, ++retriedCount, context);
            } catch (IOException e) {
                exception = e;
                retry = retryHandler.retryRequest(exception, ++retriedCount, context);
            } catch (NullPointerException e) {
                exception = new IOException(e.getMessage());
                exception.initCause(e);
                retry = retryHandler.retryRequest(exception, ++retriedCount, context);
            } catch (HttpException e) {
                throw e;
            } catch (Throwable e) {
                exception = new IOException(e.getMessage());
                exception.initCause(e);
                retry = retryHandler.retryRequest(exception, ++retriedCount, context);
            }
            if (!retry) {
                throw new HttpException(exception);
            }
        }
    }

    @Override
    protected Void doInBackground(Object... params) {
        if (this.state == State.CANCELLED || params == null || params.length == 0) return null;

        if (params.length > 3) {
            fileSavePath = String.valueOf(params[1]);
            isDownloadingFile = fileSavePath != null;
            autoResume = (Boolean) params[2];
            autoRename = (Boolean) params[3];
        }

        try {
            if (this.state == State.CANCELLED) return null;
            // init request & requestUrl
            request = (HttpRequestBase) params[0];
            requestUrl = request.getURI().toString();
            if (callback != null) {
                callback.setRequestUrl(requestUrl);
            }

            this.publishProgress(UPDATE_START);

            lastUpdateTime = SystemClock.uptimeMillis();
            ResponseInfo<T> responseInfo = sendRequest(request);
            if (responseInfo != null) {
                this.publishProgress(UPDATE_SUCCESS, responseInfo);
                return null;
            }
        } catch (HttpException e) {
            this.publishProgress(UPDATE_FAILURE, e, e.getMessage());
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onProgressUpdate(Object... values) {
        if (this.state == State.CANCELLED || values == null || values.length == 0 || callback == null)
            return;
        switch ((Integer) values[0]) {
            case UPDATE_START:
                this.state = State.STARTED;
                callback.onStart();
                break;
            case UPDATE_LOADING:
                if (values.length != 3) return;
                this.state = State.LOADING;
                callback.onLoading(
                        Long.valueOf(String.valueOf(values[1])),
                        Long.valueOf(String.valueOf(values[2])),
                        isUploading);
                break;
            case UPDATE_FAILURE:
                if (values.length != 3) return;
                this.state = State.FAILURE;
                callback.onFailure((HttpException) values[1], (String) values[2]);
                break;
            case UPDATE_SUCCESS:
                if (values.length != 2) return;
                this.state = State.SUCCESS;
                callback.onSuccess((ResponseInfo<T>) values[1]);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private ResponseInfo<T> handleResponse(HttpResponse response) throws HttpException, IOException {
        if (response == null) {
            throw new HttpException("response is null");
        }
        if (isCancelled()) return null;

        StatusLine status = response.getStatusLine();

        int statusCode = status.getStatusCode();
        if (statusCode <= 300) {
            Object result = getResult(response);

            return new ResponseInfo<T>(response, (T) result, false);
        } else if ((statusCode == 301 || statusCode == 302)) {
            if (response.containsHeader("Location")) {
                if (httpRedirectHandler == null) {
                    httpRedirectHandler = new DefaultHttpRedirectHandler();
                }
                HttpRequestBase request = httpRedirectHandler.getDirectRequest(response);
                if (request != null) {
                    return this.sendRequest(request);
                }
            }


        } else if (statusCode == 400) {
            return new ResponseInfo<T>(response, (T) response.getHeaders("message")[0].getValue(), false);
        } else if (statusCode == 416) {
            throw new HttpException(statusCode, "maybe the file has downloaded completely");
        } else if (statusCode == 500) {
            return new ResponseInfo<T>(response, (T) "statusCode == 500", false);
        } else {
            Log.e(TAG, "http error statusCode = " + statusCode);
            Log.e(TAG, "http error Reason = " + status.getReasonPhrase());
            throw new HttpException(statusCode, status.getReasonPhrase());
        }
        return null;
    }

    private Object getResult(HttpResponse response) throws IOException {
        Object result = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
//  todo  添加的cookie 解析过程
            isUploading = false;
            if (isDownloadingFile) {
                autoResume = autoResume && OtherUtils.isSupportRange(response);
                String responseFileName = autoRename ? OtherUtils.getFileNameFromHttpResponse(response) : null;
                FileDownloadHandler downloadHandler = new FileDownloadHandler();
                result = downloadHandler.handleEntity(entity, this, fileSavePath, autoResume, responseFileName);
            } else {
                StringDownloadHandler downloadHandler = new StringDownloadHandler();
                result = downloadHandler.handleEntity(entity, this, charset);
                boolean cachedEnabled = HttpUtils.sHttpCache.isEnabled(requestMethod);
                if (cachedEnabled) {
                    HttpUtils.sHttpCache.put(requestUrl, (String) result, expiry);
                }
                Log.e(TAG, "正在请求的expiry=" + (cachedEnabled ? expiry : "不可用") + ",url=" + requestUrl);
            }
        }
        return result;
    }


    /**
     * cancel request task.
     */
    @Override
    public void cancel() {
        this.state = State.CANCELLED;

        if (request != null && !request.isAborted()) {
            try {
                request.abort();
            } catch (Throwable e) {
            }
        }
        if (!this.isCancelled()) {
            try {
                this.cancel(true);
            } catch (Throwable e) {
            }
        }

        if (callback != null) {
            callback.onCancelled();
        }
    }

    @Override
    public boolean updateProgress(long total, long current, boolean forceUpdateUI) {
        if (callback != null && this.state != State.CANCELLED) {
            if (forceUpdateUI) {
                this.publishProgress(UPDATE_LOADING, total, current);
            } else {
                long currTime = SystemClock.uptimeMillis();
                if (currTime - lastUpdateTime >= callback.getRate()) {
                    lastUpdateTime = currTime;
                    this.publishProgress(UPDATE_LOADING, total, current);
                }
            }
        }
        return this.state != State.CANCELLED;
    }

    public enum State {
        WAITING(0), STARTED(1), LOADING(2), FAILURE(3), CANCELLED(4), SUCCESS(5);
        private int value = 0;

        State(int value) {
            this.value = value;
        }

        public static State valueOf(int value) {
            switch (value) {
                case 0:
                    return WAITING;
                case 1:
                    return STARTED;
                case 2:
                    return LOADING;
                case 3:
                    return FAILURE;
                case 4:
                    return CANCELLED;
                case 5:
                    return SUCCESS;
                default:
                    return FAILURE;
            }
        }

        public int value() {
            return this.value;
        }
    }

    private static final class NotUseApacheRedirectHandler implements RedirectHandler {
        @Override
        public boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
            return false;
        }

        @Override
        public URI getLocationURI(HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
            return null;
        }
    }
}
