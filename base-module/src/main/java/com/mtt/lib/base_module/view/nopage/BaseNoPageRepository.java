package com.mtt.lib.base_module.view.nopage;

import androidx.lifecycle.MutableLiveData;

import com.uber.autodispose.ObservableSubscribeProxy;
import com.mtt.lib.base_module.core.BaseBean;
import com.mtt.lib.base_module.core.ShowStateType;
import com.mtt.lib.base_module.http.RxUtils;
import com.mtt.lib.base_module.mvvm.BaseRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mtt on 2019-12-02
 * Describe
 */
public class BaseNoPageRepository<A> extends BaseRepository<A> {
    public static final int TYPE_DATA_HTTP=10;
    public static final int TYPE_DATA_NATIVE=11;
    public <D> MutableLiveData<BaseBean<NoPageInterface<D>>> getData(Observable observable, int type){
        switch (type){
            case TYPE_DATA_HTTP:
                return getDataHttp(observable);
            case TYPE_DATA_NATIVE:
                return getDataNative(observable);

        }
        return null;
    }


    public <D> MutableLiveData<BaseBean<NoPageInterface<D>>> getDataHttp(Observable<BaseBean<NoPageInterface<D>>> observable){
        final MutableLiveData<BaseBean<NoPageInterface<D>>> liveData = new MutableLiveData<>();
        Disposable subscribe = ((ObservableSubscribeProxy<BaseBean>) observable.subscribeOn(Schedulers.io()).compose(RxUtils.checkResponseResult()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
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

    public <D> MutableLiveData<BaseBean<NoPageInterface<D>>> getDataNative(Observable<List<D>> observable){

        Observable<BaseBean<NoPageInterface<D>>> observable1 = observable.subscribeOn(Schedulers.io()).
                flatMap(new Function<List<D>, ObservableSource<BaseBean<NoPageInterface<D>>>>() {
            @Override
            public ObservableSource<BaseBean<NoPageInterface<D>>> apply(List<D> userEntities) throws Exception {
                BaseBean<NoPageInterface<D>> baseBean = new BaseBean<>();
                baseBean.setBody(new NoPageInterface<D>() {
                    @Override
                    public List<D> getList() {
                        return userEntities;
                    }
                });
                BaseBean.ResultBean resultBean = new BaseBean.ResultBean();
                resultBean.setCode(1);
                baseBean.setResult(resultBean);

                return Observable.just(baseBean);
            }
        });
        return getDataHttp(observable1);
    }

}
