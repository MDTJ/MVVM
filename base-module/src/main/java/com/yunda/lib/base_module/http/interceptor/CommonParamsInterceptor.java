package com.yunda.lib.base_module.http.interceptor;

import com.yunda.lib.base_module.data.BaseProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;


public class CommonParamsInterceptor implements Interceptor {
    private final BaseProvider provider;
    public CommonParamsInterceptor(BaseProvider provider) {
        this.provider = provider;
    }
    /**
     * 拦截进行加密
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取请求体
        Request request = chain.request();
        //请求方法
        String method = request.method();
        Request.Builder requestBuilder = request.newBuilder();
        //公共参数
        Map<String, String> params = new HashMap<>();

        //接受所有公共参数
            addCommonParams(params);
        if (method.equals("GET")) {
            HttpUrl url = request.url();

            Set<String> oldParameter = url.queryParameterNames();
            for (String key : oldParameter) {
                params.put(key, url.queryParameter(key));
            }
//            params.put("sign", GetMd5(params, BuildConfig.APP_SECRET));

            HttpUrl.Builder httpUrlBuilder = url.newBuilder();

            for (Object o : params.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                httpUrlBuilder.removeAllQueryParameters((String) entry.getKey());
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            request = requestBuilder.build();

        } else if (method.equals("POST")) {

            if (request.body() instanceof FormBody) {
                FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
                //获取提交的旧数据
                FormBody oldFormBody = (FormBody) request.body();
                int paramSize = oldFormBody.size();
                if (paramSize > 0) {
                    for (int i = 0; i < paramSize; i++) {
//                        newFormBodyBuilder.add(oldFormBody.name(i), oldFormBody.value(i));
                        params.put(oldFormBody.name(i), oldFormBody.value(i));
                    }
                }
//                params.put("sign", GetMd5(params, BuildConfig.APP_SECRET));
                //重新装入
                for (Object o : params.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    newFormBodyBuilder.add((String) entry.getKey(), (String) entry.getValue());
                }

                requestBuilder.post(newFormBodyBuilder.build());
                request = requestBuilder.build();
            }else if(request.body() instanceof MultipartBody){

            }
            else {
                FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
//                params.put("sign", GetMd5(params, BuildConfig.APP_SECRET));
                //重新装入
                for (Object o : params.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    newFormBodyBuilder.add((String) entry.getKey(), (String) entry.getValue());
                }

                requestBuilder.post(newFormBodyBuilder.build());
                request = requestBuilder.build();
            }
        }
        return chain.proceed(request);
    }

    public void addCommonParams(Map<String, String> params) {
//        params.put("app", BuildConfig.appKey);
//        params.put("version", BuildConfig.versionName);
//        params.put("deviceType", BuildConfig.DEVICE_TYPE);
//        params.put("uniqueId", provider.getUniqueId());
//        params.put("appName", BuildConfig.appName);
        //添加 token

//        if (provider.getToken() != null && provider.getToken().length() > 0) {
//            params.put("mobileAccessToken", provider.getToken());
//        }
//        if (provider.getUserId() != null && provider.getUserId().length() > 0 && !provider.getUserId().equals("null")) {
//            params.put("userID", provider.getUserId());
//        }

    }

}
