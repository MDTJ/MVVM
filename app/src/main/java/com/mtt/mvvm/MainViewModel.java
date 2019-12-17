package com.mtt.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.mtt.lib.base_module.core.BaseBean;
import com.mtt.lib.base_module.db.entity.UserEntity;
import com.mtt.lib.base_module.mvvm.BaseViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public class MainViewModel extends BaseViewModel<MainApiService, MainRepository> {


    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseBean<UserBean>> getData(){
        return repository.getHttpData(apiService.getData());
    }


    public void insert(UserEntity entity){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                repository.insert(entity);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public LiveData<List<UserEntity>> query(){
        return repository.getNativeData();

    }
}
