package com.yunda.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yunda.lib.base_module.mvvm.BaseMVVMActivity;
import com.yunda.mvvm.databinding.ActivityMainBinding;


public class MainActivity extends BaseMVVMActivity<MainViewModel, ActivityMainBinding,UserBean> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void setListener() {
        viewDataBinding.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observe(viewModel.getData(), new OnCallBack() {
                    @Override
                    public void onSuccess(UserBean body) {
                        viewDataBinding.setUser(body);
                    }
                });
            }
        });

    }


    @Override
    public void initData() {
        setToolBarTitle("Main");
    }
}
