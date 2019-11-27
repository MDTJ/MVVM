package com.yunda.lib.base_module.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.uber.autodispose.AutoDisposeConverter;
import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.http.OkHttpUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public abstract class BaseViewModel<A,M> extends AndroidViewModel {
    private CompositeDisposable compositeDisposable;
    protected A apiService;
    protected AutoDisposeConverter<BaseBean<M>> autoDisposeConverter;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        Class apiClass;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            apiClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            apiService= (A) OkHttpUtils.getRetrofit().create(apiClass);
        }

    }

    public void setAutoDisposeConverter(AutoDisposeConverter autoDisposeConverter) {
       this.autoDisposeConverter=autoDisposeConverter;
    }


    protected void addSubscribe(Disposable subscription) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }
        this.compositeDisposable.add(subscription);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
