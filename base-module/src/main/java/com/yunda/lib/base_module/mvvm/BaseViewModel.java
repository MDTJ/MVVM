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
import timber.log.Timber;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public abstract class BaseViewModel<A,R extends BaseRepository<A>> extends AndroidViewModel {
    private CompositeDisposable compositeDisposable;
    protected A apiService;
    protected R repository;
    protected AutoDisposeConverter<BaseBean> autoDisposeConverter;


    public BaseViewModel(@NonNull Application application) {
        super(application);


        Class apiClass;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            apiClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            try{
                apiService= (A) OkHttpUtils.getRetrofit().create(apiClass);
            }catch (Exception e){
                Timber.e(e);
            }

        }
        repository=createRepository();
        createCompositeDisposable();
        repository.setCompositeDisposable(compositeDisposable);
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



    protected R createRepository() {
        Class apiClass;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            apiClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            try {
                return ((R) apiClass.newInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;

    }



    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
