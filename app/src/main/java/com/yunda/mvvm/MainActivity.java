package com.yunda.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yunda.lib.base_module.mvvm.BaseMVVMActivity;
import com.yunda.mvvm.databinding.ActivityMainBinding;
import com.yunda.mvvm.db.DBInstance;
import com.yunda.mvvm.db.UserEntity;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseMVVMActivity<MainViewModel, ActivityMainBinding,UserBean> {

    UserEntity userEntity;
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


    public void insert(View view) {
        userEntity = new UserEntity();
        userEntity.setName("mtt");
        userEntity.setAddress("aaa");
        Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return DBInstance.getInstance().getUserDao().insert(userEntity);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long integer) throws Exception {
                Log.e("asadad","    "+integer);
            }
        });
    }

    public void query(View view) {
        DBInstance.getInstance().getUserDao().getByUid2().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<UserEntity>>() {
            @Override
            public void accept(List<UserEntity> list) throws Exception {
                Log.e("asadad",list.size()+"");
            }
        });

    }

    public void query2(View view) {
        DBInstance.getInstance().getUserDao().getAllByLivedata().observe(this, new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> list) {
                Log.e("Livedata",list.size()+"");
            }
        });
    }
}
