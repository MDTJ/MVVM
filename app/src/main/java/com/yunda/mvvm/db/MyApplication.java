package com.yunda.mvvm.db;

import android.app.Application;
import android.content.Context;

/**
 * Created by mtt on 2019-11-27
 * Describe
 */
public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getInstance(){
        return context;
    }



}
