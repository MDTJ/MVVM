package com.mtt.lib.base_module.core;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.BaseDialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.mtt.lib.view.emptyview.EmptyViewLayout;

/**
 * Created by mtt on 2019-11-22
 * Describe
 */
public abstract class BaseEmptyActivity<VB extends ViewDataBinding> extends BaseActivity<VB> implements EmptyViewLayout.ErrorViewListener{

    protected EmptyViewLayout mEmptyViewLayout;
    private BaseDialogFragment baseDialogFragment;

    protected void initEmptyViewLayout(View view) {
        mEmptyViewLayout = new EmptyViewLayout(this, view);
        mEmptyViewLayout.setErrorListener(this);
        setEmptyView(mEmptyViewLayout);
    }

    protected void setEmptyView(EmptyViewLayout emptyView){
    }
    protected void initEmptyViewLayout(@IdRes int id) {
        initEmptyViewLayout(findViewById(id));
    }

    @Override
    public void errorButtonListener() {

    }

    public void showEmpty() {
        hideOtherLoading();
        if (mEmptyViewLayout != null) {
            mEmptyViewLayout.showEmpty();
        }
    }

    public void showDataError(String msg) {
        hideOtherLoading();
        if (mEmptyViewLayout != null){
            mEmptyViewLayout.showEmpty();
//            mEmptyViewLayout.setDataErrorMessage(msg);
        }
    }

    public void showNetError(String msg) {
        hideOtherLoading();
        if (mEmptyViewLayout != null){
            mEmptyViewLayout.showDataError();
//            mEmptyViewLayout.showNetError();
//            mEmptyViewLayout.setNetErrorMessage(msg);
        }
    }

    public void showContent() {
        hideOtherLoading();
        if (mEmptyViewLayout != null) mEmptyViewLayout.showContent();
    }

    public void showLoading() {
        if (mEmptyViewLayout != null) mEmptyViewLayout.showLoading();
    }

    public void hideOtherLoading() {
        dismissDialogLoading();
    }


    public  void showDialogLoading() {
        hideOtherLoading();
        if (baseDialogFragment == null) {
            baseDialogFragment = new BaseDialogFragment();
        }
        if(baseDialogFragment.getDialog()==null){
            baseDialogFragment.show(getSupportFragmentManager(), "");
        }

    }

    public void dismissDialogLoading() {
        if (baseDialogFragment != null&&baseDialogFragment.getDialog()!=null && baseDialogFragment.getDialog().isShowing())
        {
            baseDialogFragment.dismissAllowingStateLoss();

        }

        baseDialogFragment = null;
    }
    public void showToast(String msg) {
        hideOtherLoading();
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void showWaiting() {
        showDialogLoading();

    }

    public abstract class OnCallBack<M> implements BaseEmptyView<M> {

        @Override
        public void showEmpty() {
            BaseEmptyActivity.this.showEmpty();

        }

        @Override
        public void showDataError(String msg) {
            BaseEmptyActivity.this.showDataError(msg);
        }

        @Override
        public void showNetError(String msg) {
            BaseEmptyActivity.this.showNetError(msg);
        }

        @Override
        public void showContent() {
            BaseEmptyActivity.this.showContent();
        }

        @Override
        public void showLoading() {
            BaseEmptyActivity.this.showLoading();
        }

        @Override
        public void showToast(String msg) {
            BaseEmptyActivity.this.showToast(msg);
        }

        @Override
        public void showWaiting() {

            BaseEmptyActivity.this.showWaiting();
        }

    }

    protected <M> void observe(LiveData<BaseBean<M>> liveData, OnCallBack onCallBack) {
        liveData.observe(this, new Observer<BaseBean<M>>() {
            @Override
            public void onChanged(BaseBean<M> mBaseBean) {
                M body = mBaseBean.getBody();
                Throwable throwable = mBaseBean.getThrowable();
                switch (mBaseBean.getType()) {
                    case ShowStateType.TYPE_SHOWCONTENT:
                        onCallBack.showContent();
                        onCallBack.onSuccess(body);
                        break;
                    case ShowStateType.TYPE_SHOWWAITTING:
                        onCallBack.showWaiting();
                        break;
                    case ShowStateType.TYPE_SHOWDATAERROR:
                        onCallBack.showDataError(throwable.getMessage());
                        break;
                    case ShowStateType.TYPE_SHOWEMPTY:
                        onCallBack.showEmpty();
                        break;
                    case ShowStateType.TYPE_SHOWLOADING:
                        onCallBack.showLoading();
                        break;
                    case ShowStateType.TYPE_SHOWNETERROR:
                        onCallBack.showNetError(throwable.getMessage());
                        break;
                    case ShowStateType.TYPE_SHOWTOAST:
                        onCallBack.showToast(throwable.getMessage());
                        break;

                }

            }
        });


    }

}
