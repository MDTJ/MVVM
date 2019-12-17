package com.mtt.lib.base_module.mvvm;

import androidx.lifecycle.MutableLiveData;

import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.ObservableSubscribeProxy;
import com.mtt.lib.base_module.core.BaseBean;
import com.mtt.lib.base_module.core.ShowStateType;
import com.mtt.lib.base_module.http.RxUtils;

import io.reactivex.Observable;
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
                baseBean.setType(ShowStateType.TYPE_SHOWWAITTING);
                liveData.postValue(baseBean);
            }
        }).as(autoDisposeConverter)).subscribe(new Consumer<BaseBean>() {
            @Override
            public void accept(BaseBean beanBaseBean) throws Exception {
                if(beanBaseBean.getBody()==null){
                    beanBaseBean.setType(ShowStateType.TYPE_SHOWTOAST);
                    beanBaseBean.setThrowable(new Throwable("数据异常"));
                }else {
                    beanBaseBean.setType(ShowStateType.TYPE_SHOWCONTENT);
                    liveData.postValue(beanBaseBean);
                }

            }
        },new RxUtils.SimpleToastThrowable(liveData));
        addSubscribe(subscribe);
        return liveData;
    }


     protected void addSubscribe(Disposable subscription) {
        this.compositeDisposable.add(subscription);
    }

}
