package com.yunda.lib.base_module.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

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
    protected BaseRepository<A,M> repository;
    protected AutoDisposeConverter<BaseBean<M>> autoDisposeConverter;


    public BaseViewModel(@NonNull Application application) {
        super(application);
        repository=createRepository();
        createCompositeDisposable();
        repository.setCompositeDisposable(compositeDisposable);

        Class apiClass;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            apiClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            apiService= (A) OkHttpUtils.getRetrofit().create(apiClass);

        }
        repository.setApiService(apiService);

    }

    private void createCompositeDisposable() {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }
    }

    public void setAutoDisposeConverter(AutoDisposeConverter autoDisposeConverter) {
       repository.setAutoDisposeConverter(autoDisposeConverter);
    }



    protected <R extends BaseRepository<A,M>> R createRepository() {
        return ((R) new BaseRepository<A, M>());
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
