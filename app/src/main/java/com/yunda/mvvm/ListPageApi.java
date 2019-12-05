package com.yunda.mvvm;

import com.yunda.lib.base_module.core.BaseBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by mtt on 2019-12-03
 * Describe
 */
public interface ListPageApi {
    @FormUrlEncoded
    @POST("getList")
    Observable<BaseBean<TestListBean>> getList(@Field("pageNum") String pageNum);
}
