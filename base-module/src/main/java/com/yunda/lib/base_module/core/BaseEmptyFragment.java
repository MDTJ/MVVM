package com.yunda.lib.base_module.core;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.BaseDialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.yunda.lib.view.emptyview.EmptyViewLayout;

/**
 * Created by mtt on 2019-11-28
 * Describe
 */
public abstract class BaseEmptyFragment<VB extends ViewDataBinding> extends BaseFragment<VB> implements EmptyViewLayout.ErrorViewListener {
    protected EmptyViewLayout mEmptyViewLayout;
    private BaseDialogFragment baseDialogFragment;

    protected void initEmptyViewLayout(View view) {
        mEmptyViewLayout = new EmptyViewLayout(getActivity(), view);
        mEmptyViewLayout.setErrorListener(this);
        setEmptyView(mEmptyViewLayout);
    }

    protected void setEmptyView(EmptyViewLayout emptyView) {
    }

    protected void initEmptyViewLayout(@IdRes int id) {
        initEmptyViewLayout(dataBinding.getRoot().findViewById(id));
    }

    @Override
    public void errorButtonListener() {
       showContent();
    }

    public void showEmpty() {
        hideOtherLoading();
        if (mEmptyViewLayout != null) {
            mEmptyViewLayout.showEmpty();
        }
    }

    public void showDataError(String msg) {
        hideOtherLoading();
        if (mEmptyViewLayout != null) {
            mEmptyViewLayout.showEmpty();
//            mEmptyViewLayout.setDataErrorMessage(msg);
        }
    }

    public void showNetError(String msg) {
        hideOtherLoading();
        if (mEmptyViewLayout != null) {
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

    public void loadMoreError(){

    }


    public void showDialogLoading() {
        hideOtherLoading();
        if (baseDialogFragment == null) {
            baseDialogFragment = new BaseDialogFragment();
        }
        if (baseDialogFragment.getDialog() == null) {
            baseDialogFragment.show(getFragmentManager(), "");
        }

    }

    public void dismissDialogLoading() {
        if (baseDialogFragment != null && baseDialogFragment.getDialog() != null && baseDialogFragment.getDialog().isShowing()) {
            baseDialogFragment.dismissAllowingStateLoss();

        }

        baseDialogFragment = null;
    }

    public void showToast(String msg) {
        hideOtherLoading();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showWaiting() {
        showDialogLoading();

    }

    public abstract class OnCallBack<M> implements BaseEmptyView<M> {

        @Override
        public void showEmpty() {
            BaseEmptyFragment.this.showEmpty();

        }

        @Override
        public void showDataError(String msg) {
            BaseEmptyFragment.this.showDataError(msg);
        }

        @Override
        public void showNetError(String msg) {
            BaseEmptyFragment.this.showNetError(msg);
        }

        @Override
        public void showContent() {
            BaseEmptyFragment.this.showContent();
        }

        @Override
        public void showLoading() {
            BaseEmptyFragment.this.showLoading();
        }

        @Override
        public void showToast(String msg) {
            BaseEmptyFragment.this.showToast(msg);
        }

        @Override
        public void showWaiting() {

            BaseEmptyFragment.this.showWaiting();
        }
        public void loadMoreError(){
            BaseEmptyFragment.this.loadMoreError();
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
                    case ShowStateType.TYPE_LOADMOREERROR:
                        onCallBack.loadMoreError();
                        break;

                }

            }
        });


    }
}
