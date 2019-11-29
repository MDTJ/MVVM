package com.yunda.lib.debuglogin_module;


import com.yunda.test_module.utils.LoginUtils;

/**
 * Created by mtt on 2018/5/18.
 */

public class DebugLogin {
    public void doLogin(final OnLoginSuccessListener loginSuccessListener){
        new LoginUtils().doLogin(() -> loginSuccessListener.onLoginSuccessListener());
    }

    public interface OnLoginSuccessListener{
        void onLoginSuccessListener();
    }


}
