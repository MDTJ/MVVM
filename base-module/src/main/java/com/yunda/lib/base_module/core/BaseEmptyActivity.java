package com.yunda.lib.base_module.core;

import android.util.Log;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public abstract class BaseEmptyActivity<VB extends ViewDataBinding,M> extends BaseActivity<VB> {



    public abstract  class OnCallBack implements BaseEmptyView<M>{

        @Override
        public void showEmpty() {
            Log.e("asdadad","showEmpty");

        }

        @Override
        public void showDataError(String msg) {
            Log.e("asdadad","showDataError");
        }

        @Override
        public void showNetError(String msg) {
            Log.e("asdadad","showNetError");
        }

        @Override
        public void showContent() {
            Log.e("asdadad","showContent");
        }

        @Override
        public void showLoading() {
            Log.e("asdadad","showLoading");
        }

        @Override
        public void showToast(String msg) {
            Log.e("asdadad","showToast");
        }

        @Override
        public void showWaiting() {
            Log.e("asdadad","showWaiting");
        }

    }
    protected  void observe(MutableLiveData<BaseBean<M>> mutableLiveData,OnCallBack onCallBack){

        mutableLiveData.observe(this, new Observer<BaseBean<M>>() {
            @Override
            public void onChanged(BaseBean<M> mBaseBean) {
                M body = mBaseBean.getBody();
                switch (mBaseBean.getType()){
                    case 0:
                        onCallBack.showContent();
                        onCallBack.onSuccess(body);
                        break;
                    case 1:
                        onCallBack.showWaiting();
                }

            }
        });


    }

}
