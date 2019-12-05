package com.yunda.lib.base_module.view.page;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.mvvm.BaseViewModel;

import io.reactivex.Observable;

/**
 * Created by mtt on 2019-12-03
 * Describe
 */
public abstract class BasePageViewModel<A> extends BaseViewModel<A,BasePageRepository<A>> {

    public int getNextPage(){
        return repository.getNextPage();
    }
    public int getTotalNum(){
        return repository.getTotalNum();
    }
    public void setNextPage(int nextPage){
        repository.setNextPage(nextPage);
    }
    public BasePageViewModel(@NonNull Application application) {
        super(application);
    }

    public <D> LiveData<BaseBean<PageInterface<D>>> getData(int type){
        return repository.getData(getObservable(),type);
    }

    protected abstract Observable getObservable();

    @Override
    protected BasePageRepository<A> createRepository() {
        return new BasePageRepository<A>();
    }
}
