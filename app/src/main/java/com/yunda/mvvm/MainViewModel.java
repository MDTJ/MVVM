package com.yunda.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.yunda.lib.arouter_module.DBInstance;
import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.db.entity.UserEntity;
import com.yunda.lib.base_module.http.RxUtils;
import com.yunda.lib.base_module.mvvm.BaseRepository;
import com.yunda.lib.base_module.mvvm.BaseViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public class MainViewModel extends BaseViewModel<MainApiService,MainRepository> {


    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseBean<UserBean>> getData(){
        return repository.getHttpData(apiService.getData());
    }


    public void insert(UserEntity entity){
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                DBInstance.getInstance().getUserDao().insert(entity);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Action() {
            @Override
            public void run() throws Exception {
                Log.e("adadad","insertSuccess");
            }
        });
    }

    public LiveData<List<UserEntity>> query(){
        return repository.getNativeData();

    }
}
