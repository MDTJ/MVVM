package com.yunda.test_module.login;

import com.yunda.lib.base_module.core.BaseBean;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by mtt on 2018/5/22.
 */

public interface LoginApiService {
    @POST("login")
    Observable<BaseBean> login();

}
