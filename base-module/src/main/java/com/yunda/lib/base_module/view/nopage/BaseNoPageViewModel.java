package com.yunda.lib.base_module.view.nopage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.mvvm.BaseViewModel;

import io.reactivex.Observable;

/**
 * Created by mtt on 2019-12-02
 * Describe
 */
public abstract class BaseNoPageViewModel<A> extends BaseViewModel<A,BaseNoPageRepository<A>> {
    public BaseNoPageViewModel(@NonNull Application application) {
        super(application);
    }
    public <D> LiveData<BaseBean<NoPageInterface<D>>> getData(int type){
       return repository.getData(getObservable(),type);
    }

    protected abstract Observable getObservable();

    @Override
    protected BaseNoPageRepository<A> createRepository() {
        return new BaseNoPageRepository<A>();
    }
}
