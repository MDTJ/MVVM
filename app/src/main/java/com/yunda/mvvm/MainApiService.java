package com.yunda.mvvm;



import com.yunda.lib.base_module.core.BaseBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public interface MainApiService {

    @POST("getUser")
    Observable<BaseBean<UserBean>> getData();

    @POST("getUser")
    Observable<BaseBean<UserBean>> getData(@Field("file")String a);
}