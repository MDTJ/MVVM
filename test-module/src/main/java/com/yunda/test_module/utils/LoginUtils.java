package com.yunda.test_module.utils;

import android.util.Log;

import com.yunda.lib.base_module.BuildConfig;
import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.data.BaseSp;

import com.yunda.lib.base_module.http.OkHttpUtils;
import com.yunda.lib.base_module.http.RxUtils;
import com.yunda.test_module.login.LoginApiService;
import com.yunda.test_module.login.LoginBean;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by mtt on 2018/5/18.
 */

public class LoginUtils {


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

    public void doLogin(final OnLoginSuccessListener onLoginSuccessListener) {
        OkHttpUtils.getRetrofit().create(LoginApiService.class).login()
                .compose(RxUtils.checkResponseResult())
                .compose(RxUtils.supportSchedulers())
                .subscribe(new Consumer<BaseBean>() {
                    @Override
                    public void accept(BaseBean baseBean) throws Exception {
                        onLoginSuccessListener.onLoginSuccessListener();
                    }
                });

    }

    public interface OnLoginSuccessListener {
        void onLoginSuccessListener();
    }
}
