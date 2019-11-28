package com.yunda.lib.base_module.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.ObservableSubscribeProxy;
import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.http.RxUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableConverter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mtt on 2019-11-28
 * Describe
 */
public class BaseRepository<A> {
    private CompositeDisposable compositeDisposable;
    protected A apiService;
    protected AutoDisposeConverter autoDisposeConverter;


     void setAutoDisposeConverter(AutoDisposeConverter autoDisposeConverter) {
        this.autoDisposeConverter = autoDisposeConverter;
    }

     void setCompositeDisposable(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    void setApiService(A apiService) {
        this.apiService = apiService;
    }

    public <M> MutableLiveData<BaseBean<M>> getHttpData(Observable<BaseBean<M>> observable){
        final MutableLiveData<BaseBean<M>> liveData = new MutableLiveData<>();
        Disposable subscribe = ((ObservableSubscribeProxy<BaseBean<M>>) observable.subscribeOn(Schedulers.io()).compose(RxUtils.checkResponseResult()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                BaseBean baseBean = new BaseBean();
                baseBean.setType(1);
                liveData.postValue(baseBean);
            }
        }).as(autoDisposeConverter)).subscribe(new Consumer<BaseBean>() {
            @Override
            public void accept(BaseBean beanBaseBean) throws Exception {
                beanBaseBean.setType(0);
                liveData.postValue(beanBaseBean);
            }
        },new RxUtils.SimpleToastThrowable(liveData));
        addSubscribe(subscribe);
        return liveData;
    }


     void addSubscribe(Disposable subscription) {
        this.compositeDisposable.add(subscription);
    }

}
