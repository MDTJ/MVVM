package com.yunda.lib.base_module.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mtt on 2019-11-07
 * Describe
 */
public class UserAgentInterrceptor  implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .removeHeader("User-Agent")//移除旧的
                .addHeader("User-Agent",System.getProperty("http.agent"))//添加真正的头部
                .build();
        return chain.proceed(request);
    }
}
