package com.yunda.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.http.RxUtils;
import com.yunda.lib.base_module.mvvm.BaseViewModel;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public class MainViewModel extends BaseViewModel<MainApiService,UserBean> {


    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<BaseBean<UserBean>> getData(){
        return repository.getHttpData(apiService.getData());
    }

}
