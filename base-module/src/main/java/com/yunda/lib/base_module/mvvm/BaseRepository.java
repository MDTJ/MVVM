package com.yunda.lib.base_module.mvvm;

import androidx.lifecycle.MutableLiveData;
import com.uber.autodispose.AutoDisposeConverter;
import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.http.RxUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mtt on 2019-11-28
 * Describe
 */
public class BaseRepository<A,M> {
    private CompositeDisposable compositeDisposable;
    protected A apiService;
    protected AutoDisposeConverter<BaseBean<M>> autoDisposeConverter;


    public void setAutoDisposeConverter(AutoDisposeConverter<BaseBean<M>> autoDisposeConverter) {
        this.autoDisposeConverter = autoDisposeConverter;
    }

    public void setCompositeDisposable(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    public void setApiService(A apiService) {
        this.apiService = apiService;
    }

    public MutableLiveData<BaseBean<M>> getHttpData(Observable<BaseBean<M>> observable){
        final MutableLiveData<BaseBean<M>> liveData = new MutableLiveData<>();
        Disposable subscribe = observable.subscribeOn(Schedulers.io()).compose(RxUtils.checkResponseResult()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                BaseBean baseBean=new BaseBean();
                baseBean.setType(1);
                liveData.postValue(baseBean);
            }
        }).as(autoDisposeConverter).subscribe(new Consumer<BaseBean<M>>() {
            @Override
            public void accept(BaseBean<M> beanBaseBean) throws Exception {
                beanBaseBean.setType(0);
                liveData.postValue(beanBaseBean);
            }
        },new RxUtils.SimpleToastThrowable(liveData));
        addSubscribe(subscribe);
        return liveData;
    }


    protected void addSubscribe(Disposable subscription) {
        this.compositeDisposable.add(subscription);
    }



}
