package com.yunda.lib.base_module.view.page;

import androidx.lifecycle.MutableLiveData;

import com.uber.autodispose.ObservableSubscribeProxy;
import com.yunda.lib.base_module.core.BaseBean;
import com.yunda.lib.base_module.core.ShowStateType;
import com.yunda.lib.base_module.http.RxUtils;
import com.yunda.lib.base_module.mvvm.BaseRepository;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mtt on 2019-12-03
 * Describe
 */
public class BasePageRepository<A> extends BaseRepository<A> {
    private int nextPage = 1;
    private int totalNum = 0;
    private int PAGE_NUMBER = 20;

    public int getTotalNum() {
        return totalNum;
    }

    public static final int TYPE_DATA_HTTP=10;
    public static final int TYPE_DATA_NATIVE=11;
    public <D> MutableLiveData<BaseBean<PageInterface<D>>> getData(Observable observable, int type){
        switch (type){
            case TYPE_DATA_HTTP:
                return getDataHttp(observable);
            case TYPE_DATA_NATIVE:
                return null;

        }
        return null;
    }
    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public <D> MutableLiveData<BaseBean<PageInterface<D>>> getDataHttp(Observable<BaseBean<PageInterface<D>>> observable){
        final MutableLiveData<BaseBean<PageInterface<D>>> liveData = new MutableLiveData<>();
        Disposable subscribe = ((ObservableSubscribeProxy<BaseBean>) observable.subscribeOn(Schedulers.io()).compose(RxUtils.checkResponseResult()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                BaseBean baseBean=new BaseBean();
                baseBean.setType(ShowStateType.TYPE_SHOWLOADING);
                liveData.postValue(baseBean);
            }
        }).as(autoDisposeConverter)).subscribe(new Consumer<BaseBean>() {
            @Override
            public void accept(BaseBean beanBaseBean) throws Exception {
                if (beanBaseBean.getBody() == null || ((PageInterface<D>) beanBaseBean.getBody()).getList() == null) {
                    if (nextPage == 1) {
                        beanBaseBean.setType(ShowStateType.TYPE_SHOWDATAERROR);
                        beanBaseBean.setThrowable(new Throwable("数据异常"));
                    } else {
                        beanBaseBean.setType(ShowStateType.TYPE_LOADMOREERROR);
                        beanBaseBean.setThrowable(new Throwable("数据异常"));
                    }
                    liveData.postValue(beanBaseBean);
                } else {
                    beanBaseBean.setType(ShowStateType.TYPE_SHOWCONTENT);
                    totalNum = ((PageInterface<D>) beanBaseBean.getBody()).getTotal();
                    nextPage++;
                    liveData.postValue(beanBaseBean);
                }

            }
        }, new RxUtils.DealThrowable(liveData) {
            @Override
            protected void netError(Throwable throwable) {
                BaseBean beanBaseBean =new BaseBean();
                if (nextPage == 1) {
                    beanBaseBean.setType(ShowStateType.TYPE_SHOWDATAERROR);
                    beanBaseBean.setThrowable(new Throwable("数据异常"));
                } else {
                    beanBaseBean.setType(ShowStateType.TYPE_LOADMOREERROR);
                    beanBaseBean.setThrowable(new Throwable("数据异常"));
                }
                liveData.postValue(beanBaseBean);
            }

            @Override
            protected void dataError(Throwable throwable) {
                BaseBean beanBaseBean =new BaseBean();
                if (nextPage == 1) {
                    beanBaseBean.setType(ShowStateType.TYPE_SHOWDATAERROR);
                    beanBaseBean.setThrowable(new Throwable("数据异常"));
                } else {
                    beanBaseBean.setType(ShowStateType.TYPE_LOADMOREERROR);
                    beanBaseBean.setThrowable(new Throwable("数据异常"));
                }
                liveData.postValue(beanBaseBean);
            }

            @Override
            protected void otherError(Throwable throwable) {
                BaseBean beanBaseBean =new BaseBean();
                if (nextPage == 1) {
                    beanBaseBean.setType(ShowStateType.TYPE_SHOWDATAERROR);
                    beanBaseBean.setThrowable(new Throwable("数据异常"));
                } else {
                    beanBaseBean.setType(ShowStateType.TYPE_LOADMOREERROR);
                    beanBaseBean.setThrowable(new Throwable("数据异常"));
                }
                liveData.postValue(beanBaseBean);
            }
        });
        addSubscribe(subscribe);
        return liveData;
    }
}
