package com.mtt.lib.base_module.http;


import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mtt.lib.base_module.core.BaseBean;
import com.mtt.lib.base_module.core.ShowStateType;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * Created by mtt on 2019/11/22.
 */

public class RxUtils {
    private RxUtils() {
    }

    public static <T> ObservableTransformer<T, T> supportSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 判断 result code 是否正常
     */
    public static <T extends BaseBean> ObservableTransformer<T, T> checkResponseResult() {
        return upstream -> upstream.map((Function<T, T>) t -> {
            if (t == null || t.getResult() == null) {
                throw new ServerError("服务器异常，数据为空", ResponseCode.ERROR_CODE);
            }
            if (t.getResult().getCode() == ResponseCode.SUCCESS_CODE) { //成功  1
                return t;
            } else {
                throw new ServerError(t.getResult().getMsg(), t.getResult().getCode());
            }
        });
    }

    /**
     * 通过 BaseBean 拿到　body
     */
    public static <T> ObservableTransformer<BaseBean<T>, T> transformerResult() {
        return upstream -> upstream.flatMap((Function<BaseBean<T>, ObservableSource<T>>) resultBean -> {
            return createData(resultBean.getBody());
        });
    }


    private static <T> Observable<T> createData(final T t) {
        return Observable.create(emitter -> {
            try {
                if (t == null) {
                    try {
                        emitter.onNext((T) new String(""));
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(new ServerError("服务器数据为null", ResponseCode.ERROR_CODE));
                    }
                } else {
                    emitter.onNext(t);
                    emitter.onComplete();
                }

            } catch (Exception e) {
                emitter.onError(e);
            }

        });
    }


    public static <T> ObservableTransformer<BaseBean<T>, T> simpleTransformer() {
        return upstream -> upstream
                .compose(RxUtils.checkResponseResult())
                .compose(RxUtils.<T>transformerResult());

    }


    public static <T> ObservableTransformer<BaseBean<String>, List<T>> simpleListTransformer(final String key, final Class<T> tClass) {
        return upstream -> {
            //转 list
            return upstream
                    .compose(RxUtils.<BaseBean<String>>checkResponseResult()) //判断 异常
                    .map(stringBaseBean -> {
                        JSONObject jsonObject = JSON.parseObject(stringBaseBean.getBody());
                        List<T> ts = JSONObject.parseArray(jsonObject.getString(key), tClass);
                        if (ts == null){
                            ts = Collections.emptyList();
                        }
                        return ts;
                    });
        };
    }


    public abstract static class DealThrowable<M> implements Consumer<Throwable> {
        MutableLiveData<BaseBean> liveData;
        BaseBean value;

        public DealThrowable(MutableLiveData<BaseBean> liveData) {
            this.liveData = liveData;
            value=new BaseBean();
        }

        @Override
        public void accept(@NonNull Throwable throwable) throws Exception {

            if (isNetError(throwable)) {
                Timber.tag("http request error").e(throwable, "-----------netError---------------------!! ");
                if (throwable instanceof HttpException) {
                    int code = ((HttpException) throwable).code();
                    if (code > 400) {
                        Exception dataError = new Exception("服务器异常");
                        dataError(dataError);
                        return;
                    }
                }
                Exception netError = null;
                if (throwable instanceof SocketTimeoutException) {
                    netError = new Exception("请求超时，请检查网络");
                }else {
                    netError=new Exception("您的网络好像有问题!");
                }
                netError(netError);
            } else if (throwable instanceof ServerError) { //应该判断一下服务器500 提示服务器异常
                if (((ServerError) throwable).code == ResponseCode.USER_IS_OTHER_LOGIN) { //踢出

                } else {
                    Timber.tag("http request error").e(throwable, "-----------dataError---------------------!! ");
                    dataError(throwable);
                }
            } else {
                Timber.tag("http request error").e(throwable, "-----------otherError---------------------!! ");
                otherError(throwable);
            }

        }

        protected abstract void netError(Throwable throwable);

        protected abstract void dataError(Throwable throwable);

        protected abstract void otherError(Throwable throwable);
    }

    public static class SimpleDealThrowable<M> extends DealThrowable {
        public SimpleDealThrowable(MutableLiveData<BaseBean> liveData) {
            super(liveData);
        }

        @Override
        protected void netError(Throwable throwable) {
            if (throwable == null){
                throwable = new Exception("您的网络好像有问题!");
            }
            value.setThrowable(throwable);
            value.setType(ShowStateType.TYPE_SHOWNETERROR);
            liveData.postValue(value);

        }

        @Override
        protected void dataError(Throwable throwable) {
            value.setThrowable(throwable);
            value.setType(ShowStateType.TYPE_SHOWDATAERROR);
            liveData.postValue(value);

        }

        @Override
        protected void otherError(Throwable throwable) {
            value.setThrowable(throwable);
            value.setType(ShowStateType.TYPE_SHOWDATAERROR);
            liveData.postValue(value);
        }
    }

    public static class SimpleToastThrowable<M> extends DealThrowable {
        public SimpleToastThrowable(MutableLiveData<BaseBean> liveData) {
            super(liveData);
        }

        @Override
        protected void netError(Throwable throwable) {
            if (throwable == null){
                throwable = new Exception("您的网络好像有问题!");
            }
            value.setThrowable(throwable);
            value.setType(ShowStateType.TYPE_SHOWTOAST);
            liveData.postValue(value);

        }

        @Override
        protected void dataError(Throwable throwable) {
            value.setThrowable(throwable);
            value.setType(ShowStateType.TYPE_SHOWTOAST);
            liveData.postValue(value);
        }

        @Override
        protected void otherError(Throwable throwable) {
            value.setThrowable(throwable);
            value.setType(ShowStateType.TYPE_SHOWTOAST);
            liveData.postValue(value);


        }
    }


    private static boolean isNetError(Throwable throwable) {
        return throwable instanceof HttpException || throwable instanceof SocketTimeoutException || throwable instanceof ConnectException||throwable instanceof UnknownHostException;
    }

}
