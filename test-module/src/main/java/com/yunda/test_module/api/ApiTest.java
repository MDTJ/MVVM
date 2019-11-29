package com.yunda.test_module.api;

import android.os.Build;

import com.yunda.lib.base_module.BuildConfig;
import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.http.RxUtils;
import com.yunda.test_module.login.LoginApiService;
import com.yunda.test_module.login.LoginBean;
import com.yunda.test_module.utils.RxTestUtils;

import io.reactivex.functions.Consumer;

/**
 * Created by mtt on 2018/5/27.
 */

public class ApiTest extends BaseApiTest {


    private boolean isOnline() {
        return BuildConfig.ISONLINE;
    }

    protected String getFirstVal() {
        if (isOnline()) {
            return "";
        } else {
            return "";
        }
    }

    protected String getSecondVal() {
        if (isOnline()) {
            return "";
        } else {
            return "";
        }
    }

    LoginApiService loginApiService;


    @Override
    public void startTest() {
        super.startTest();
        RxTestUtils.asyncToSync();
        loginApiService = getRetrofit().create(LoginApiService.class);
        loginApiService.login()
                .compose(RxUtils.checkResponseResult())
                .compose(RxUtils.supportSchedulers())
                .subscribe(new Consumer<BaseBean>() {
                    @Override
                    public void accept(BaseBean baseBean) throws Exception {

                    }
                });
    }

}
