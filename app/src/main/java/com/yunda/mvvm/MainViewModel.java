package com.yunda.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
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
        final MutableLiveData<BaseBean<UserBean>> liveData = new MutableLiveData<>();
        Disposable subscribe = apiService.getData().subscribeOn(Schedulers.io()).compose(RxUtils.checkResponseResult()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                BaseBean baseBean=new BaseBean();
                baseBean.setType(1);
                liveData.postValue(baseBean);
            }
        }).as(autoDisposeConverter).subscribe(new Consumer<BaseBean<UserBean>>() {
            @Override
            public void accept(BaseBean<UserBean> beanBaseBean) throws Exception {
                beanBaseBean.setType(0);
                liveData.postValue(beanBaseBean);
            }
        },new RxUtils.SimpleToastThrowable(liveData));
        addSubscribe(subscribe);
        return liveData;
    }

}
