package com.yunda.lib.base_module.http;


import android.os.Environment;

import com.yunda.lib.base_module.data.BaseProvider;
import com.yunda.lib.base_module.data.BaseSp;
import com.yunda.lib.base_module.http.fastjson.FastJsonConverterFactory;
import com.yunda.lib.base_module.http.interceptor.BaseLoggingInterceptor;
import com.yunda.lib.base_module.http.interceptor.CommonParamsInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by mtt on 2018/5/22.
 */

public class OkHttpUtils {

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;


    private static OkHttpClient getOkHttpClient() {
        return getOkHttpClient(BaseSp.getInstance());
    }

    public static OkHttpClient getOkHttpClient(BaseProvider provider) {
        if (okHttpClient == null) {
            BaseLoggingInterceptor logging = new BaseLoggingInterceptor(message -> System.out.println(message));

            okHttpClient = new OkHttpClient().newBuilder()
                    .cache(new Cache(new File(Environment.getDataDirectory() + "/okhttp_cache/"), 50 * 1024 * 1024))
                    .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .readTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new CommonParamsInterceptor(provider))
                    .addInterceptor(logging)
                    .build();
        }
        return okHttpClient;
    }

    public static Retrofit getRetrofit(String url, OkHttpClient okHttpClient) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofit() {
        return getRetrofit(getOkHttpClient());
    }

    public static Retrofit getRetrofit(OkHttpClient okHttpClient) {
        return getRetrofit("http://10.20.143.129:8060/", okHttpClient);
    }

}
