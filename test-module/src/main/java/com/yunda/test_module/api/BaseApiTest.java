package com.yunda.test_module.api;

import com.yunda.lib.base_module.data.BaseProvider;
import com.yunda.lib.base_module.http.OkHttpUtils;

import org.junit.Before;

import retrofit2.Retrofit;

/**
 * Created by mtt on 2018/5/27.
 */

public abstract class BaseApiTest implements BaseProvider {


    protected Retrofit mRetrofit;

    @Before
    public void startTest() {
        mRetrofit = OkHttpUtils.getRetrofit(OkHttpUtils.getOkHttpClientOfTest(this));
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

}
