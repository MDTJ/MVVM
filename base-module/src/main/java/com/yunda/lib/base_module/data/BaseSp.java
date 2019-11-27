package com.yunda.lib.base_module.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mtt on 2018/5/22.
 */

public class BaseSp implements BaseProvider {
    private static final String SP_NAME = "project";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String LOGIN_USER_CODE = "login_user_code";



    private static class SingletonHolder {
        private static final BaseSp INSTANCE = new BaseSp();
    }

    public static final BaseSp getInstance() {
        return BaseSp.SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        if (sharedPreferences != null) {
            return;
        }
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void setToken(String token) {
        editor.putString("AccessToken", token);
        editor.commit();
    }


    /**
     * 用户登录的用户名
     *
     * @param userCode
     */
    public void setUserCode(String userCode) {
        sharedPreferences.edit().putString(LOGIN_USER_CODE, userCode).commit();
    }



    public void setUserId(String userId) {
        editor.putString("Login_Userid", userId);
        editor.commit();
    }

    public String getUserId() {
        return this.sharedPreferences.getString("Login_Userid", "");
    }




}
