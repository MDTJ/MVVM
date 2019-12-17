package com.mtt.mvvm;



import com.mtt.lib.base_module.core.BaseBean;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public interface MainApiService {

    @POST("getUser")
    Observable<BaseBean<UserBean>> getData();

}
